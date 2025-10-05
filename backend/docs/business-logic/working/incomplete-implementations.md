# 대기 생성 및 노쇼/제재 시스템 미완성 구현 목록 (Application/Domain Layer)

## 작성일: 2025-10-05
## 최종 업데이트: 2025-10-05
## 상태: 작업 중 (Working Document)

이 문서는 현재 대기 신청, 노쇼 처리, 제재 시스템과 관련된 미완성 구현 사항을 정리합니다.
**Infrastructure Layer 구현은 별도 작업으로 진행됩니다.**

---

## ✅ 완료된 작업

### 1. 당일 노쇼 예약 재신청 로직 구현 ✅

**완료일**: 2025-10-05

**구현 내용**:
- `WaitingQuery.forMemberAndPopupOnDate()` 메서드 추가
- `WaitingService`에서 당일 예약 상태별 분석 로직 구현
- 노쇼 1개만 있는 경우 재신청 허용

**코드 위치**: `WaitingService.java:61-77`

**문서 업데이트**: `waiting-application-business-logic.md:55-84` (TODO 제거 완료)

### 2. 노쇼 후 예상 대기 시간 업데이트 ✅

**완료일**: 이미 구현됨 (TODO 주석만 제거)

**구현 내용**:
- `Waiting.minusWaitingNumber(PopupWaitingStatistics)` 메서드에 이미 구현되어 있음
- 대기 번호 감소 시 자동으로 예상 시간 재계산
- `PopupWaitingStatistics.calculateExpectedWaitingTime()` 활용

**코드 위치**:
- `Waiting.java:147-172` - minusWaitingNumber 메서드
- `ScheduledNoShowProcessService.java:188-204` - 노쇼 처리 시 호출

**문서 업데이트**: `ban-system-business-logic.md:54-128` (노쇼 감지/처리 로직 상세 추가)

---

## 1. 논리적 구현 누락

### 1.1 제재 만료 자동 정리 로직 누락

**현재 상태**:
- `Ban.isActive()` 메서드로 만료 여부 확인
- 만료된 제재 레코드 삭제/정리 로직 없음

**제안**:
1. **스케줄러 추가**:
   ```java
   @Scheduled(cron = "0 0 3 * * *")  // 매일 새벽 3시
   public void cleanupExpiredBans() {
       List<Ban> allBans = banPort.findAll();
       List<Ban> expiredBans = allBans.stream()
           .filter(ban -> !ban.isActive())
           .toList();

       if (!expiredBans.isEmpty()) {
           banPort.deleteAll(expiredBans);
           log.info("만료된 제재 {}건 삭제 완료", expiredBans.size());
       }
   }
   ```

2. **BanPort에 메서드 추가**:
   ```java
   public interface BanPort {
       Ban save(Ban ban);
       List<Ban> findByQuery(BanQuery query);
       List<Ban> findAll();              // 추가
       void deleteAll(List<Ban> bans);   // 추가
   }
   ```

**대안**: Soft Delete 방식
- `deleted` 플래그 추가
- 만료된 제재는 조회에서만 제외, 실제 삭제는 안 함
- 제재 이력 보존 가능

---

### 1.2 글로벌 제재 카운트 초기화 로직 명확화 필요

**현재 구현**: `ScheduledNoShowProcessService.java:128-149`
```java
LocalDateTime lastGlobalBannedAt = globalBanHistory.stream()
    .filter(ban -> !ban.isActive())  // 만료된 제재만
    .map(Ban::getBannedAt)
    .max(Comparator.naturalOrder())
    .orElse(null);

int banCount = banPort.findByQuery(new BanQuery(
    waiting.member().id(),
    BanType.STORE,
    null,
    lastGlobalBannedAt,  // 이 시간 이후의 스토어 제재만 카운트
    false
)).size();
```

**잠재적 문제**:
1. **활성 글로벌 제재 중 스토어 제재 발생 시**:
   - 현재: 마지막 **만료된** 글로벌 제재 이후로 카운트
   - 의도: 마지막 글로벌 제재(활성 포함) 이후로 카운트해야 할 수도 있음

2. **명확화 필요**:
   ```java
   // 옵션 1: 활성 글로벌 제재 있으면 카운트 리셋
   Optional<Ban> activeGlobalBan = globalBanHistory.stream()
       .filter(Ban::isActive)
       .findFirst();

   if (activeGlobalBan.isPresent()) {
       // 활성 글로벌 제재 있으면 스토어 제재 카운트 = 0
       return;
   }

   // 옵션 2: 마지막 글로벌 제재(활성+만료) 이후로 카운트
   LocalDateTime lastGlobalBannedAt = globalBanHistory.stream()
       .map(Ban::getBannedAt)
       .max(Comparator.naturalOrder())
       .orElse(null);
   ```

---

## 2. 임시 코드 및 리팩토링 필요 항목

### 2.1 임시 컨트롤러 코드

**위치**:
- `PopupController.java:34, 70`
- `ImageController.java:23, 33`

```java
//TODO 임시 코드 삭제 필요
```

**조치 필요**: 프로덕션 배포 전 삭제

---

### 2.2 아키텍처 리팩토링

**위치**: `SecurityConfig.java:51`
```java
// TODO: 아키텍처 리팩토링 https://github.com/JECT-Study/JECT-3th-6team/pull/99
```

**위치**: `PopupService.java:48`
```java
// TODO: https://github.com/JECT-Study/JECT-3th-6team/pull/92#discussion_r2210591165
```

**위치**: `PopupFilterRequest.java:11`
```java
// TODO: https://github.com/JECT-Study/JECT-3th-6team/pull/92#discussion_r2210604215
```

**위치**: `PopupQuery.java:12`
```java
// TODO: https://github.com/JECT-Study/JECT-3th-6team/pull/92#discussion_r2210630218
```

**조치 필요**: PR 리뷰 코멘트 확인 및 반영

---

### 2.3 기타 리팩토링

**위치**: `PopupDtoMapper.java:23`
```java
// TODO : 리팩토링 필요
```

**위치**: `CursorResult.java:6`
```java
//TODO : 현장대기에서도 사용하도록 리팩토링
```

**위치**: `WaitingPortAdapter.java:39, 47, 71`
```java
public List<Waiting> saveAll(List<Waiting> waitings) { // TODO 성능 고려 필요
// TODO 다른 기술을 활용한 동적 쿼리 작성 필요
// TODO 성능 고려 해야 함.
```

**위치**: `PopupPortAdapter.java:308`
```java
// TODO: 연관된 모든 엔티티(location, schedule 등)를 함께 삭제하는 로직 구현 필요
```

**위치**: `Popup.java:24`
```java
// TODO: 조기 종료 상태를 별도 boolean 필드로 관리하는 대신, PopupStatus에 EARLY_CLOSED 상태를 추가하는 것을 검토
```

---

## 3. 우선순위별 작업 목록

### 🟡 Medium Priority (개선 필요)
1. **글로벌 제재 카운트 로직 명확화** - 엣지 케이스 처리
2. **제재 만료 자동 정리** - 데이터 정리 (선택 사항)

### 🟢 Low Priority (코드 품질)
3. **임시 코드 제거** - 코드 품질
4. **리팩토링 항목 처리** - 아키텍처 개선

---

## 4. 다음 작업 제안

### Phase 1: 논리적 개선 (Domain/Application Layer)
- [ ] 글로벌 제재 카운트 로직 명확화
- [ ] 제재 만료 정리 스케줄러 (선택)

### Phase 2: 코드 품질 개선
- [ ] 임시 코드 제거
- [ ] TODO 리팩토링 처리

---

**작성자**: Claude Code Analysis
**검토 필요**: 개발팀 전체
**업데이트 주기**: 구현 진행에 따라 수시 업데이트
