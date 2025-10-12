# 대기 신청 및 제재 시스템 비즈니스 로직

## 개요

이 문서는 팝업 대기 신청과 제재 시스템의 비즈니스 로직을 설명합니다. 사용자가 팝업에 대기 신청을 할 때 수행되는 검증 과정과 악성 사용자에 대한 제재 시스템을 다룹니다.

## 1. 대기 신청 비즈니스 로직

### 1.1 대기 신청 검증 단계

대기 신청 시 다음 순서로 검증이 수행됩니다:

1. **입력값 유효성 확인**
   - 팝업 존재 여부 확인
   - 회원 정보 유효성 확인
   - 대기자 이름, 이메일, 인원수 등 필드 검증

2. **팝업 운영 시간 확인**
   - `Popup.isOpenAt(LocalDateTime)` 메서드로 현재 시점 운영 여부 확인
   - 조기 종료(`earlyClosed`) 필드가 true인 경우 운영 중단으로 판단
   - 팝업의 날짜 범위(`DateRange`) 및 요일별 운영시간(`WeeklyOpeningHours`) 검증

3. **제재 여부 확인**
   - **스토어 제재**: `BanQuery.byMemberAndPopup(memberId, popupId)`로 특정 팝업 제재 확인
   - **글로벌 제재**: `BanQuery.byMemberIdFromAll(memberId)`로 전체 팝업 제재 확인
   - 둘 중 하나라도 활성 제재가 있으면 신청 거부

4. **중복 예약 확인**
   - 같은 날(`LocalDate`) 같은 팝업에 대한 예약 중복 방지
   - `WaitingQuery.forDuplicateCheck(memberId, popupId, date)` 사용
   - TODO: 당일 노쇼 처리된 예약 1개만 있는 경우 예외 처리 필요

### 1.2 대기 신청 처리

검증 통과 후 다음 단계로 대기 신청을 처리합니다:

1. **대기 번호 할당**
   - `WaitingPort.getNextWaitingNumber(popupId)`로 다음 순번 조회
   - 대기자가 없으면 0번, 있으면 마지막 번호 + 1

2. **예상 대기시간 계산**
   - `WaitingStatisticsPort.findCompletedStatisticsByPopupId(popupId)` 호출
   - `PopupWaitingStatistics.calculateExpectedWaitingTime(waitingNumber)` 실행
   - 통계 데이터가 부족하면 null 반환

3. **대기 정보 생성 및 저장**
   - 모든 정보를 포함한 `Waiting` 객체 생성
   - `WaitingPort.save()` 호출하여 영속화

4. **알림 처리**
   - `WaitingNotificationService.processWaitingCreatedNotifications()` 호출
   - 대기 신청 완료 알림 발송

## 2. 제재 시스템 비즈니스 로직

### 2.1 제재 도메인 모델

#### Ban 도메인 모델 구조
```java
public class Ban {
    private final Long id;
    private final Member member;           // 제재 대상 회원 (직접 참조)
    private final BanType type;           // GLOBAL | STORE
    private final LocalDateTime bannedAt;  // 제재 시작 시간
    private final int durationDays;       // 제재 기간 (일 단위)
    private final Popup popup;            // 스토어 제재 시 대상 팝업
}
```

#### 제재 유형
- **GLOBAL**: 모든 팝업에 대한 예약 금지
- **STORE**: 특정 팝업에 대한 예약 금지

### 2.2 제재 활성 여부 판단

제재의 활성 여부는 다음 로직으로 결정됩니다:
```java
public boolean isActive() {
    return LocalDateTime.now().isBefore(getExpiresAt());
}

public LocalDateTime getExpiresAt() {
    return bannedAt.plusDays(durationDays);
}
```

### 2.3 제재 조회 시스템

#### BanQuery 활용
- **글로벌 제재 조회**: `BanQuery.byMemberIdFromAll(memberId)`
- **스토어 제재 조회**: `BanQuery.byMemberAndPopup(memberId, popupId)`

#### 제재 확인 로직
대기 신청 시 다음과 같이 제재를 확인합니다:
```java
boolean notPopupBan = banPort.findByQuery(BanQuery.byMemberAndPopup(memberId, popupId)).isEmpty();
boolean notGlobalBan = banPort.findByQuery(BanQuery.byMemberIdFromAll(memberId)).isEmpty();

if (!notPopupBan || !notGlobalBan) {
    throw new BusinessException(ErrorType.BANNED_MEMBER);
}
```

## 3. 예상 대기시간 계산 시스템

### 3.1 통계 데이터 구조

#### WaitingStatistics 도메인 모델
```java
public class WaitingStatistics {
    private final Long popupId;
    private final Long waitingId;
    private final int initialWaitingNumber;  // 최초 부여된 대기 순번
    private final LocalDateTime reservedAt;  // 예약 시점
    private final LocalDateTime enteredAt;   // 입장 시점
}
```

#### 시간 계산 메서드
- `getTotalWaitingTime()`: 총 대기시간 = enteredAt - reservedAt
- `getTimePerPerson()`: 1명당 대기시간 = 총 대기시간 / 최초 대기 순번

### 3.2 일급 컬렉션: PopupWaitingStatistics

특정 팝업의 입장 완료된 통계들을 관리하는 일급 컬렉션:

```java
public class PopupWaitingStatistics {
    private final Long popupId;
    private final List<WaitingStatistics> statistics; // 입장 완료된 통계만

    // 평균 대기시간(분/명) 계산
    public Double calculateAverageTimePerPerson();

    // 예상 대기시간 계산
    public Integer calculateExpectedWaitingTime(int currentWaitingNumber);
}
```

### 3.3 예상 대기시간 계산 공식

1. **개별 통계의 1명당 시간 계산**
   ```
   1명당 시간 = (입장 시점 - 예약 시점) / 최초 대기 순번
   ```

2. **팝업별 평균 계산**
   ```
   평균 대기시간 = 모든 완료된 예약의 1명당 시간의 평균
   ```

3. **현재 대기자의 예상 시간**
   ```
   예상 대기시간 = 현재 대기 순번 × 평균 대기시간 (올림 처리)
   ```

## 4. 예상 대기시간 시스템 연동

### 4.1 대기 신청 시 예상 시간 계산

대기 신청이 완료되면 기존 통계 데이터를 활용하여 초기 예상 대기시간을 계산합니다:

```java
Integer expectedWaitingTime = waitingStatisticsPort.findCompletedStatisticsByPopupId(request.popupId())
        .calculateExpectedWaitingTime(nextWaitingNumber);
```

### 4.2 통계 데이터 활용

- **신규 팝업**: 통계 데이터가 없어 예상 시간이 null로 설정
- **기존 팝업**: 과거 입장 완료 데이터를 기반으로 계산된 예상 시간 제공
- **정확도 향상**: 입장 처리가 진행될수록 통계 데이터가 축적되어 정확도 개선

## 5. 팝업 운영시간 관리

### 5.1 팝업 운영시간 확인

`Popup.isOpenAt(LocalDateTime)` 메서드는 다음을 확인합니다:

1. **조기 종료 확인**
   ```java
   if (earlyClosed) {
       return false;
   }
   ```

2. **전체 운영 기간 확인**
   ```java
   LocalDateTime endOfDay = schedule.dateRange().endDate().atTime(23, 59, 59);
   if (dateTime.isBefore(schedule.dateRange().startDate().atStartOfDay()) ||
       dateTime.isAfter(endOfDay)) {
       return false;
   }
   ```

3. **요일별 운영시간 확인**
   ```java
   return schedule.weeklyOpeningHours()
           .getOpeningHours(dateTime.getDayOfWeek())
           .map(openingHours -> {
               LocalDateTime openDateTime = dateTime.toLocalDate().atTime(openingHours.openTime());
               LocalDateTime closeDateTime = dateTime.toLocalDate().atTime(openingHours.closeTime());
               return !dateTime.isBefore(openDateTime) && dateTime.isBefore(closeDateTime);
           })
           .orElse(false);
   ```

### 5.2 조기 종료 개념

- `earlyClosed` boolean 필드로 관리
- TODO: PopupStatus에 EARLY_CLOSED 상태 추가 검토 필요
- 조기 종료 시 운영시간에 관계없이 예약 불가

## 6. 에러 처리 및 예외 상황

### 6.1 대기 신청 관련 주요 에러 타입

- `POPUP_NOT_FOUND`: 존재하지 않는 팝업
- `POPUP_NOT_OPENED`: 팝업 비운영 시간
- `BANNED_MEMBER`: 제재된 회원
- `DUPLICATE_WAITING`: 중복 예약
- `MEMBER_NOT_FOUND`: 존재하지 않는 회원
- `INVALID_PEOPLE_COUNT`: 유효하지 않은 인원수
- `INVALID_WAITING_PERSON_NAME`: 유효하지 않은 대기자 이름

### 6.2 예외 상황 처리

1. **통계 데이터 부족**
   - 새로운 팝업의 경우 예상 대기시간이 null로 설정
   - 첫 번째 대기자에게도 정상적으로 서비스 제공

2. **제재 상태 변경**
   - 대기 신청 후 제재가 추가된 경우의 처리 방안 필요
   - 제재 해제 시 대기 신청 재개 정책

3. **팝업 운영시간 변경**
   - 대기 신청 후 팝업 조기 종료 시 대기자 알림
   - 운영시간 연장 시 대기 처리 방안

## 7. 트랜잭션 관리

### 7.1 대기 신청 트랜잭션

대기 신청의 모든 단계는 하나의 트랜잭션에서 처리됩니다:
1. 팝업 및 회원 정보 검증
2. 제재 상태 확인
3. 중복 예약 확인
4. 대기 정보 생성 및 저장
5. 알림 처리

실패 시 모든 변경사항이 롤백되어 데이터 일관성을 보장합니다.

### 7.2 제재 데이터 일관성

- 제재 정보 조회 시점과 대기 신청 시점 간의 일관성 보장
- 동시 제재 적용 시 경쟁 조건 방지

## 8. 향후 개선 사항

1. **제재 시스템 확장**
   - 제재 사유별 분류 및 단계별 제재 시스템
   - 자동 해제 및 경고 시스템

2. **대기 신청 최적화**
   - 대기 번호 할당 로직 성능 개선
   - 동시 신청 시 처리 성능 향상

3. **실시간 검증**
   - 팝업 상태 변경 시 실시간 대기자 알림
   - 제재 적용 시 즉시 대기 취소 처리

4. **통계 시스템 고도화**
   - 시간대별, 요일별 예상 대기시간 세분화
   - 머신러닝 기반 예측 정확도 향상