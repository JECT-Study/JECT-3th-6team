# 대기열 입장 처리 비즈니스 로직

## 개요

대기열 입장 처리는 대기번호가 0인 대기자가 실제로 팝업스토어에 입장할 때 수행되는 핵심 비즈니스 로직입니다. 입장 처리 과정에서 대기 상태 변경, 순번 재정렬, 그리고 다양한 검증 과정을 거치며, 실패 시 구체적인 예외를 통해 명확한 피드백을 제공합니다.

## 입장 처리 프로세스

### 전체 프로세스 흐름

```
요청 수신 → 대기 정보 조회 → 입장 자격 검증 → 입장 처리 → 순번 재정렬 → 완료
```

### 단계별 상세 로직

#### 1. 대기 정보 조회
```java
// WaitingService.java:141-145
WaitingQuery query = WaitingQuery.forWaitingId(request.waitingId());
Waiting waiting = waitingPort.findByQuery(query)
        .stream()
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorType.WAITING_NOT_FOUND, String.valueOf(request.waitingId())));
```
- **목적**: 입장 처리할 대기 정보가 존재하는지 확인
- **실패 시**: `WAITING_NOT_FOUND` 예외 발생
- **구현**: `WaitingQuery.forWaitingId()` 사용하여 단건 조회

#### 2. 입장 자격 검증
```java
// WaitingService.java:147-149
if (waiting.waitingNumber() != 0) {
    throw new BusinessException(ErrorType.WAITING_NOT_READY, String.valueOf(request.waitingId()));
}
```
- **목적**: 현재 입장 가능한 순번(0번)인지 확인
- **제약조건**: 대기번호가 정확히 0이어야 함
- **실패 시**: `WAITING_NOT_READY` 예외 발생
- **비즈니스 규칙**: 대기번호 0번만 입장 가능

#### 3. 도메인 레벨 입장 처리
```java
// WaitingService.java:150-151
Waiting enteredWaiting = waiting.enter();
```

**도메인 모델의 입장 처리 로직** (`Waiting.java:121-142`):
```java
public Waiting enter() {
    if (status != WaitingStatus.WAITING) {
        throw new BusinessException(ErrorType.INVALID_WAITING_STATUS, status.toString());
    }

    if (waitingNumber != 0) {
        throw new BusinessException(ErrorType.INVALID_WAITING_NUMBER, "대기 번호가 0이 아닙니다.");
    }

    return new Waiting(
            id, popup, waitingPersonName, member, contactEmail, peopleCount,
            waitingNumber,
            WaitingStatus.VISITED,  // 상태를 VISITED로 변경
            registeredAt,
            LocalDateTime.now(),    // 입장 시간 기록
            canEnterAt
    );
}
```
- **상태 검증**: `WAITING` 상태에서만 입장 가능
- **순번 재검증**: 도메인 레벨에서 대기번호 0 재확인
- **상태 변경**: `WAITING` → `VISITED`
- **입장 시간 기록**: `enteredAt`에 현재 시간 설정

#### 4. 입장 처리된 대기 저장
```java
// WaitingService.java:153-154
waitingPort.save(enteredWaiting);
```
- **목적**: 입장 처리된 대기 정보를 데이터베이스에 영속화
- **변경사항**: 상태(VISITED), 입장시간(enteredAt) 업데이트

#### 5. 순번 재정렬 처리
```java
// WaitingService.java:156-157
reorderWaitingNumbers(waiting.popup().getId());
```

**순번 재정렬 상세 로직** (`WaitingService.java:165-178`):
```java
private void reorderWaitingNumbers(Long popupId) {
    // 1. 해당 팝업의 모든 대기중인 대기 조회
    WaitingQuery popupQuery = WaitingQuery.forPopup(popupId, WaitingStatus.WAITING);
    List<Waiting> waitingList = waitingPort.findByQuery(popupQuery);

    // 2. 입장 처리된 대기번호보다 큰 번호들만 필터링하여 순번 앞당기기
    List<Waiting> reorderedWaitings = waitingList.stream()
            .filter(w -> w.waitingNumber() > 0)
            .map(Waiting::minusWaitingNumber)
            .toList();

    // 3. 변경된 대기들 모두 저장
    reorderedWaitings.forEach(waitingPort::save);
}
```

**도메인 모델의 순번 감소 로직** (`Waiting.java:145-169`):
```java
public Waiting minusWaitingNumber() {
    if (waitingNumber == 0) {
        throw new BusinessException(ErrorType.INVALID_WAITING_NUMBER, "대기 번호는 0 이상이어야 합니다.");
    }

    if (status != WaitingStatus.WAITING) {
        throw new BusinessException(ErrorType.INVALID_WAITING_STATUS, status.toString());
    }

    LocalDateTime canEnterAt = waitingNumber == 1 ? LocalDateTime.now() : null;

    return new Waiting(
            id, popup, waitingPersonName, member, contactEmail, peopleCount,
            waitingNumber - 1,      // 대기 번호 1 감소
            WaitingStatus.WAITING,
            registeredAt, enteredAt,
            canEnterAt              // 새로운 0번이 되면 입장 가능 시간 설정
    );
}
```

## 입장 처리 실패 시나리오

### 1. 데이터 검증 실패

#### 1.1 존재하지 않는 대기 정보 (WAITING_NOT_FOUND)
```java
ErrorType.WAITING_NOT_FOUND (HttpStatus.NOT_FOUND, "대기 정보를 찾을 수 없습니다")
```
- **발생 시점**: 대기 정보 조회 단계
- **원인**: 잘못된 waitingId 또는 삭제된 대기 정보
- **HTTP 상태**: 404 Not Found

#### 1.2 입장 자격 없음 (WAITING_NOT_READY)
```java
ErrorType.WAITING_NOT_READY (HttpStatus.BAD_REQUEST, "아직 입장할 수 없습니다.")
```
- **발생 시점**: 입장 자격 검증 단계
- **원인**: 대기번호가 0이 아닌 경우
- **HTTP 상태**: 400 Bad Request
- **비즈니스 규칙**: 대기번호 0번만 입장 가능

### 2. 도메인 규칙 위반

#### 2.1 유효하지 않은 대기 상태 (INVALID_WAITING_STATUS)
```java
ErrorType.INVALID_WAITING_STATUS (HttpStatus.BAD_REQUEST, "유효하지 않은 대기 상태입니다")
```
- **발생 시점**: 도메인 레벨 입장 처리 시 (`Waiting.enter()`)
- **원인**: `WAITING` 상태가 아닌 대기 (이미 VISITED, CANCELED 등)
- **HTTP 상태**: 400 Bad Request

#### 2.2 유효하지 않은 대기 번호 (INVALID_WAITING_NUMBER)
```java
ErrorType.INVALID_WAITING_NUMBER (HttpStatus.INTERNAL_SERVER_ERROR, "대기 번호가 유효하지 않습니다")
```
- **발생 시점**: 도메인 레벨 입장 처리 시 (`Waiting.enter()`) 또는 순번 감소 시 (`Waiting.minusWaitingNumber()`)
- **원인 1**: 입장 시 대기번호가 0이 아닌 경우 (도메인 레벨 재검증)
- **원인 2**: 순번 감소 시 대기번호가 이미 0인 경우
- **HTTP 상태**: 500 Internal Server Error

### 3. 시스템 오류

#### 3.1 데이터베이스 저장 실패
- **발생 시점**: `waitingPort.save()` 호출 시
- **원인**: DB 연결 오류, 제약조건 위반, 트랜잭션 롤백 등
- **처리**: RuntimeException으로 전파, 트랜잭션 롤백

#### 3.2 순번 재정렬 실패
- **발생 시점**: `reorderWaitingNumbers()` 실행 시
- **원인**: 동시성 문제, DB 오류 등
- **처리**: 트랜잭션 롤백으로 전체 입장 처리 취소

## 성공 조건

입장 처리가 성공하려면 다음 조건들이 모두 만족되어야 합니다:

### 1. 데이터 무결성
- ✅ 유효한 대기 ID (존재하는 대기 정보)
- ✅ 대기 상태가 `WAITING`
- ✅ 대기번호가 정확히 0

### 2. 비즈니스 규칙 준수
- ✅ 입장 순서 준수 (0번만 입장 가능)
- ✅ 상태 전이 규칙 준수 (WAITING → VISITED)

### 3. 시스템 정상 동작
- ✅ 데이터베이스 정상 동작
- ✅ 트랜잭션 성공적 처리
- ✅ 순번 재정렬 성공

## 입장 처리 결과

### 성공 시 효과
1. **대기 상태 변경**: `WAITING` → `VISITED`
2. **입장 시간 기록**: `enteredAt` 필드에 현재 시간 설정
3. **순번 재정렬**: 나머지 대기자들의 대기번호 1씩 감소
4. **새로운 0번 설정**: 기존 1번이 새로운 0번(입장 가능)이 됨
5. **입장 가능 시간 설정**: 새로운 0번의 `canEnterAt`에 현재 시간 설정

### 순번 재정렬 예시
**입장 처리 전**:
- 대기자 A: 0번 (입장 대상)
- 대기자 B: 1번
- 대기자 C: 2번
- 대기자 D: 3번

**입장 처리 후**:
- 대기자 A: VISITED 상태 (입장 완료)
- 대기자 B: 0번 (새로운 입장 가능자, `canEnterAt` 설정)
- 대기자 C: 1번
- 대기자 D: 2번

## 연관 기능

### 1. 대기 신청과의 연관성
- 대기 신청 시 생성된 대기번호가 입장 처리의 핵심 기준
- 신청 순서대로 순차적 입장 보장

### 2. 알림 시스템 연동
- 새로운 0번이 된 대기자에게 입장 가능 알림 발송
- 스케줄된 알림의 트리거 조건 변경

### 3. 자동 입장 시스템
- `ScheduledWaitingEnterService`에서 5분마다 자동 입장 처리
- 동일한 `enter()` 로직 사용하여 일관성 보장

### 4. 방문 이력 관리
- 입장 처리된 대기는 방문 이력에서 `VISITED` 상태로 조회
- `enteredAt` 시간 정보 제공

이 비즈니스 로직을 통해 시스템은 공정하고 순서가 보장되는 대기열 입장 처리 서비스를 제공할 수 있습니다.