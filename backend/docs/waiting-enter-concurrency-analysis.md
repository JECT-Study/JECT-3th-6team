# 대기열 입장 처리 동시성 분석 및 해결방안

## 문제 정의

### 비즈니스 요구사항
- 어떤 대기자가 입장 처리될 때, 같은 팝업의 나머지 대기자들의 대기 번호를 앞당겨야 함
- 예: 대기번호 1,2,3,4,5에서 3번이 입장하면 → 1,2,4,5가 1,2,3,4로 변경

### 동시성 문제점
1. **Race Condition**: 여러 입장 처리가 동시에 발생할 때 순번 재배치 충돌
2. **Lost Update**: 순번 변경 중 다른 트랜잭션의 변경사항 손실
3. **Phantom Read**: 대기열 조회와 업데이트 사이에 새로운 대기 추가/삭제
4. **Inconsistent State**: 부분적으로만 업데이트되어 순번이 중복되거나 누락

## 해결방안 분석

### 1. 비관적 락 (Pessimistic Locking)
```sql
-- 팝업의 모든 대기를 락으로 보호
SELECT * FROM waiting 
WHERE popup_id = ? AND status = 'WAITING' 
ORDER BY waiting_number 
FOR UPDATE;
```

**장점:**
- 확실한 데이터 일관성 보장
- 구현이 상대적으로 단순

**단점:**
- 성능 저하 (락 대기시간)
- 데드락 가능성
- 동시성 처리량 제한

### 2. 낙관적 락 (Optimistic Locking)
```java
@Version
private Long version;

// 업데이트 시 버전 체크
UPDATE waiting SET waiting_number = ?, version = version + 1
WHERE id = ? AND version = ?;
```

**장점:**
- 높은 동시성 처리량
- 데드락 없음

**단점:**
- 충돌 시 재시도 로직 복잡
- 대량 업데이트 시 충돌 확률 높음

### 3. 분산 락 (Distributed Lock)
```java
@RedisLock(key = "popup:#{popupId}:enter")
public void makeVisit(WaitingMakeVisitRequest request) {
    // 입장 처리 로직
}
```

**장점:**
- 멀티 인스턴스 환경에서 안전
- 애플리케이션 레벨에서 제어 가능

**단점:**
- Redis 등 외부 저장소 의존
- 락 해제 실패 시 교착상태 위험

### 4. 순번 재배치 방식 개선

#### 4-1. 즉시 재배치 vs 배치 처리
**즉시 재배치:**
```java
@Transactional
public void makeVisit(WaitingMakeVisitRequest request) {
    Waiting target = findWaiting(request.waitingId());
    target.enter();
    save(target);
    
    // 순번 앞당기기
    reorderWaitingNumbers(target.popup().id(), target.waitingNumber());
}
```

**배치 처리:**
```java
@Scheduled(fixedDelay = 30000) // 30초마다
public void reorderAllPopupWaitings() {
    List<Long> popupIds = getPopupsWithGaps();
    popupIds.forEach(this::reorderPopupWaitings);
}
```

#### 4-2. 벌크 업데이트 최적화
```sql
-- 한 번의 쿼리로 모든 순번 앞당기기
UPDATE waiting 
SET waiting_number = waiting_number - 1 
WHERE popup_id = ? 
  AND status = 'WAITING' 
  AND waiting_number > ?;
```

### 5. 이벤트 기반 아키텍처
```java
@EventListener
public void handleWaitingEntered(WaitingEnteredEvent event) {
    // 비동기로 순번 재배치 처리
    asyncReorderService.reorderWaitingNumbers(event.getPopupId(), event.getWaitingNumber());
}
```

## 권장 해결방안

### 1단계: 비관적 락 + 벌크 업데이트
```java
@Transactional
public void makeVisit(WaitingMakeVisitRequest request) {
    // 1. 팝업 단위로 락 획득
    Waiting target = waitingPort.findByIdWithLock(request.waitingId());
    
    // 2. 입장 처리
    Waiting entered = target.enter();
    waitingPort.save(entered);
    
    // 3. 벌크 업데이트로 순번 앞당기기
    waitingPort.decrementWaitingNumbers(
        target.popup().id(), 
        target.waitingNumber()
    );
}
```

### 2단계: 성능 최적화 (필요시)
```java
// Redis를 이용한 분산 락
@RedisLock(key = "popup:#{#request.popupId}:reorder", timeout = 5000)
@Transactional
public void makeVisit(WaitingMakeVisitRequest request) {
    // 입장 처리 로직
}
```

### 3단계: 이벤트 기반 최적화 (고도화)
```java
@Transactional
public void makeVisit(WaitingMakeVisitRequest request) {
    Waiting target = findWaiting(request.waitingId());
    Waiting entered = target.enter();
    waitingPort.save(entered);
    
    // 이벤트 발행 - 비동기 순번 재배치
    eventPublisher.publishEvent(new WaitingEnteredEvent(
        entered.popup().id(), 
        entered.waitingNumber()
    ));
}
```

## 구현 우선순위

### Phase 1: 최소 기능 (MVP)
- 비관적 락 + 동기 벌크 업데이트
- 단일 인스턴스 환경에서 안전성 확보

### Phase 2: 성능 개선
- Redis 분산 락 도입
- 멀티 인스턴스 환경 대응

### Phase 3: 고도화
- 이벤트 기반 비동기 처리
- 배치 처리로 성능 최적화
- 모니터링 및 알림 시스템

## 테스트 시나리오

### 동시성 테스트
```java
@Test
void concurrentEnterTest() {
    // 동시에 여러 입장 처리 요청
    CompletableFuture.allOf(
        IntStream.range(0, 10)
            .mapToObj(i -> CompletableFuture.runAsync(() -> 
                waitingService.makeVisit(new WaitingMakeVisitRequest(waitingIds.get(i)))
            ))
            .toArray(CompletableFuture[]::new)
    ).join();
    
    // 순번 일관성 검증
    assertWaitingNumbersConsistent(popupId);
}
```

### 성능 테스트
- 동시 요청 처리량 측정
- 락 대기시간 모니터링
- 데드락 발생 빈도 체크

## 모니터링 포인트

1. **성능 지표**
   - 입장 처리 응답시간
   - 동시 처리량 (TPS)
   - 락 획득 대기시간

2. **안정성 지표**
   - 데드락 발생 횟수
   - 트랜잭션 실패율
   - 데이터 일관성 오류

3. **비즈니스 지표**
   - 순번 재배치 정확도
   - 사용자 경험 지표
   - 시스템 가용성