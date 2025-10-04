# 제재 시스템 비즈니스 로직

## 개요

제재 시스템은 노쇼(No-Show) 발생 시 회원의 이용을 제한하는 기능을 제공합니다. 제재는 특정 팝업에 대한 제재(STORE)와 모든 팝업에 대한 제재(GLOBAL) 두 가지 유형으로 구분됩니다.

## 제재 유형

### 1. 스토어 제재 (STORE)
```java
// BanType.java:15
STORE  // 특정 팝업 스토어에 대한 제재
```
- **대상**: 특정 팝업에 대해서만 이용 제한
- **발동 조건**: 당일 동일 팝업에서 2회 이상 노쇼
- **제재 기간**: 1일
- **영향 범위**: 해당 팝업에만 적용

### 2. 글로벌 제재 (GLOBAL)
```java
// BanType.java:10
GLOBAL  // 전체 팝업 스토어에 대한 제재
```
- **대상**: 모든 팝업에 대해 이용 제한
- **발동 조건**: 마지막 글로벌 제재 이후 10개 이상의 스토어 제재 누적
- **제재 기간**: 3일
- **영향 범위**: 모든 팝업 적용

## 제재 도메인 모델

### Ban 도메인 객체
```java
// Ban.java:15-30
public class Ban {
    private final Long id;
    private final Member member;           // 제재 대상 회원
    private final BanType type;            // 제재 유형 (STORE/GLOBAL)
    private final LocalDateTime bannedAt;  // 제재 시작 시간
    private final int durationDays;        // 제재 기간 (일)
    private final Popup popup;             // 특정 팝업 (STORE 타입일 때만)

    public LocalDateTime getExpiresAt() {
        return bannedAt.plusDays(durationDays);  // 제재 만료 시간
    }

    public boolean isActive() {
        return LocalDateTime.now().isBefore(getExpiresAt());  // 제재 활성 여부
    }
}
```

## 제재 발동 프로세스

### 1. 노쇼 처리 흐름
```
노쇼 감지 (10분 초과) → 노쇼 상태 변경 → 순번 재정렬 → 노쇼 알림 → 스토어 제재 확인 → 글로벌 제재 확인
```

### 2. 스토어 제재 발동 (ScheduledNoShowProcessService.java:107-125)
```java
private void storeBanIfNeed(Waiting waiting, long noShowCount) {
    if (noShowCount >= 2) {  // 당일 2회 이상 노쇼
        banPort.save(
            Ban.builder()
                .bannedAt(LocalDateTime.now())
                .durationDays(1)              // 1일 제재
                .member(waiting.member())
                .popup(waiting.popup())       // 특정 팝업 지정
                .type(BanType.STORE)
                .build()
        );
    }
}
```

**발동 조건**:
- 당일 동일 팝업에서 노쇼 횟수 2회 이상
- 즉시 제재 적용 (1일간)

### 3. 글로벌 제재 발동 (ScheduledNoShowProcessService.java:127-166)
```java
private void globalBanIfNeed(Waiting waiting) {
    // 1. 마지막 글로벌 제재 시간 조회
    List<Ban> globalBanHistory = banPort.findByQuery(
        new BanQuery(waiting.member().id(), BanType.GLOBAL, null, null, false)
    );

    LocalDateTime lastGlobalBannedAt = globalBanHistory.stream()
        .filter(ban -> !ban.isActive())  // 만료된 제재만
        .map(Ban::getBannedAt)
        .max(Comparator.naturalOrder())
        .orElse(null);

    // 2. 마지막 글로벌 제재 이후 스토어 제재 개수 확인
    int banCount = banPort.findByQuery(
        new BanQuery(
            waiting.member().id(),
            BanType.STORE,
            null,
            lastGlobalBannedAt,  // 이 시간 이후의 스토어 제재만 카운트
            false
        )
    ).size();

    // 3. 10개 이상이면 글로벌 제재
    if (banCount >= 10) {
        banPort.save(
            Ban.builder()
                .bannedAt(LocalDateTime.now())
                .durationDays(3)           // 3일 제재
                .member(waiting.member())
                .type(BanType.GLOBAL)
                .build()
        );

        // 4. 글로벌 제재 알림 발송
        waitingNotificationService.sendGlobalBanNotification(waiting.member().id());
    }
}
```

**발동 조건**:
- 마지막 글로벌 제재 이후 스토어 제재 10개 이상 누적
- 글로벌 제재 이력이 없는 경우: 첫 스토어 제재부터 카운트
- 즉시 제재 적용 (3일간) 및 알림 발송

## 제재 확인 로직

### 대기 신청 시 제재 검증 (WaitingService.java:55-61)
```java
// 1. 팝업별 제재 확인
boolean notPopupBan = banPort.findByQuery(
    BanQuery.byMemberAndPopup(request.memberId(), request.popupId())
).isEmpty();

// 2. 전체 제재 확인
boolean notGlobalBan = banPort.findByQuery(
    BanQuery.byMemberIdFromAll(request.memberId())
).isEmpty();

// 3. 제재 대상이면 예외 발생
if (!notPopupBan || !notGlobalBan) {
    throw new BusinessException(ErrorType.BANNED_MEMBER, String.valueOf(request.memberId()));
}
```

**검증 순서**:
1. 해당 팝업에 대한 스토어 제재 확인
2. 모든 팝업에 적용되는 글로벌 제재 확인
3. 둘 중 하나라도 활성 제재가 있으면 신청 차단

### 제재 조회 쿼리

#### 1. 특정 회원의 글로벌 제재 조회
```java
// BanQuery.java:16-18
public static BanQuery byMemberIdFromAll(Long memberId) {
    return new BanQuery(memberId, BanType.GLOBAL, null, null, true);
}
```
- **isActiveOnly = true**: 활성 제재만 조회

#### 2. 특정 회원의 특정 팝업 제재 조회
```java
// BanQuery.java:20-22
public static BanQuery byMemberAndPopup(Long memberId, Long popupId) {
    return new BanQuery(memberId, BanType.STORE, popupId, null, true);
}
```
- **isActiveOnly = true**: 활성 제재만 조회

#### 3. 제재 이력 조회 (글로벌 제재 카운트용)
```java
// ScheduledNoShowProcessService.java:128-134
new BanQuery(
    waiting.member().id(),
    BanType.GLOBAL,
    null,
    null,
    false  // 모든 제재 이력 조회 (만료 포함)
)
```

## 제재 기간 관리

### 1. 제재 만료 시간 계산
```java
// Ban.java:23-25
public LocalDateTime getExpiresAt() {
    return bannedAt.plusDays(durationDays);
}
```

### 2. 제재 활성 상태 확인
```java
// Ban.java:27-29
public boolean isActive() {
    return LocalDateTime.now().isBefore(getExpiresAt());
}
```
- **활성 제재**: 현재 시간이 만료 시간 이전
- **만료 제재**: 현재 시간이 만료 시간 이후

## 제재 알림

### 1. 노쇼 알림
```java
// ScheduledNoShowProcessService.java:98
waitingNotificationService.processNoShowNotifications(waiting, noShowCount);
```
- 노쇼 발생 시 즉시 발송
- 노쇼 횟수 정보 포함

### 2. 글로벌 제재 알림
```java
// ScheduledNoShowProcessService.java:164
waitingNotificationService.sendGlobalBanNotification(waiting.member().id());
```
- 글로벌 제재 발동 시 즉시 발송
- 3일 제재 기간 안내

## 제재 시나리오 예시

### 시나리오 1: 스토어 제재 발동
```
1. 회원 A가 팝업 X에서 노쇼 (1회차)
   → 제재 없음

2. 회원 A가 당일 팝업 X에서 노쇼 (2회차)
   → 스토어 제재 발동 (팝업 X, 1일)
   → 팝업 Y는 신청 가능
```

### 시나리오 2: 글로벌 제재 발동
```
1. 회원 A가 여러 팝업에서 노쇼 반복
   → 스토어 제재 누적 (9개)

2. 회원 A가 추가로 다른 팝업에서 노쇼
   → 스토어 제재 1개 추가 (총 10개)
   → 글로벌 제재 발동 (3일)
   → 모든 팝업 신청 불가

3. 3일 후
   → 글로벌 제재 만료
   → 다시 신청 가능
   → 새로운 스토어 제재는 0부터 카운트 시작
```

### 시나리오 3: 제재 중 신청 시도
```
1. 회원 A가 팝업 X에 스토어 제재 중
   → 팝업 X 신청 시: BANNED_MEMBER 예외
   → 팝업 Y 신청 시: 정상 처리

2. 회원 A가 글로벌 제재 중
   → 모든 팝업 신청 시: BANNED_MEMBER 예외
```

## 제재 관련 에러

### BANNED_MEMBER
```java
ErrorType.BANNED_MEMBER (HttpStatus.FORBIDDEN, "제재된 회원입니다")
```
- **발생 시점**: 대기 신청 시 제재 확인 단계
- **원인**:
  - 해당 팝업에 대한 활성 스토어 제재 존재
  - 활성 글로벌 제재 존재
- **HTTP 상태**: 403 Forbidden

## 제재 해제

### 자동 해제
- **스토어 제재**: 1일 경과 후 자동 해제
- **글로벌 제재**: 3일 경과 후 자동 해제
- **확인 방식**: `Ban.isActive()` 메서드로 실시간 확인

### 수동 해제
- 현재 미구현
- 향후 관리자 기능으로 추가 가능

## 연관 기능

### 1. 노쇼 처리 시스템
- 스케줄러가 30초마다 노쇼 대상자 확인
- 노쇼 처리 시 제재 시스템 자동 호출
- 상세 내용: [노쇼 처리 비즈니스 로직](no-show-processing-business-logic.md)

### 2. 대기 신청
- 신청 전 제재 여부 확인
- 제재 중이면 신청 차단
- 상세 내용: [대기 신청 비즈니스 로직](waiting-application-business-logic.md)

### 3. 알림 시스템
- 노쇼 알림 발송
- 글로벌 제재 알림 발송
- 상세 내용: [알림 시스템 비즈니스 로직](notification-business-logic.md)

## 제재 카운트 초기화

### 스토어 제재 카운트
- 글로벌 제재 발동 시 초기화되지 않음
- 각 스토어 제재는 독립적으로 1일 후 만료

### 글로벌 제재 카운트
- 글로벌 제재 발동 후 새로운 기준점 설정
- 이후 스토어 제재는 이 시점부터 다시 카운트
- 10개 누적 시 재발동 가능

이 제재 시스템을 통해 노쇼 발생을 억제하고 건전한 대기 신청 문화를 조성할 수 있습니다.
