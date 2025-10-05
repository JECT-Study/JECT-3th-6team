# 대기 신청 비즈니스 로직

## 개요

대기 신청 시스템은 사용자가 팝업스토어에 현장 대기를 신청할 수 있는 기능을 제공합니다. 신청 과정에서 다양한 검증 과정을 거치며, 실패 시 구체적인 예외를 통해 사용자에게 명확한 피드백을 제공합니다.

## 대기 신청 프로세스

### 전체 프로세스 흐름

```
요청 수신 → 팝업 존재 확인 → 팝업 운영 확인 → 제재 여부 확인 → 중복 신청 확인 → 대기번호 생성 → 회원 정보 확인 → 예상 대기 시간 계산 → 대기 정보 생성 → 저장 → 알림 처리 → 응답 반환
```

### 단계별 상세 로직

#### 1. 팝업 존재 여부 확인
```java
// WaitingService.java:46-47
var popup = popupPort.findById(request.popupId())
        .orElseThrow(() -> new BusinessException(ErrorType.POPUP_NOT_FOUND, String.valueOf(request.popupId())));
```
- **목적**: 신청하려는 팝업이 실제로 존재하는지 확인
- **실패 시**: `POPUP_NOT_FOUND` 예외 발생

#### 2. 팝업 운영 확인
```java
// WaitingService.java:49-53
LocalDateTime now = LocalDateTime.now();
if (!popup.isOpenAt(now)) {
    throw new BusinessException(ErrorType.POPUP_NOT_OPENED);
}
```
- **목적**: 팝업이 현재 운영 중인지 확인
- **검증 조건**: 현재 시간이 팝업 운영 시간 범위 내인지 확인
- **실패 시**: `POPUP_NOT_OPENED` 예외 발생

#### 3. 제재 여부 확인
```java
// WaitingService.java:55-61
boolean notPopupBan = banPort.findByQuery(BanQuery.byMemberAndPopup(request.memberId(), request.popupId())).isEmpty();
boolean notGlobalBan = banPort.findByQuery(BanQuery.byMemberIdFromAll(request.memberId())).isEmpty();

if (!notPopupBan || !notGlobalBan) {
    throw new BusinessException(ErrorType.BANNED_MEMBER, String.valueOf(request.memberId()));
}
```
- **목적**: 회원이 제재 대상인지 확인
- **검증 항목**:
  - 팝업별 제재: 특정 팝업에 대한 이용 제한
  - 전체 제재: 모든 팝업에 대한 이용 제한
- **실패 시**: `BANNED_MEMBER` 예외 발생
- **참고**: 제재 시스템 상세 정보는 [제재 시스템 비즈니스 로직](ban-system-business-logic.md) 참조

#### 4. 중복 신청 확인
```java
// WaitingService.java:61-77
List<Waiting> todayWaitings = waitingPort.findByQuery(
        WaitingQuery.forMemberAndPopupOnDate(request.memberId(), request.popupId(), now.toLocalDate())
);

// 노쇼가 아닌 예약이 있는지 확인
boolean hasActiveWaiting = todayWaitings.stream()
        .anyMatch(w -> w.status() != WaitingStatus.NO_SHOW);

// 노쇼 개수 확인
long noShowCount = todayWaitings.stream()
        .filter(w -> w.status() == WaitingStatus.NO_SHOW)
        .count();

// 활성 예약이 있거나, 노쇼가 2개 이상이면 중복 신청 불가
if (hasActiveWaiting || noShowCount >= 2) {
    throw new BusinessException(ErrorType.DUPLICATE_WAITING, String.valueOf(request.popupId()));
}
```
- **목적**: 당일 동일한 회원이 같은 팝업에 이미 대기 신청했는지 확인
- **검증 범위**: 당일 기준 (날짜가 바뀌면 다시 신청 가능)
- **검증 로직**:
  1. 당일 해당 팝업의 모든 대기 내역 조회
  2. 노쇼가 아닌 활성 예약 존재 여부 확인
  3. 노쇼 예약 개수 카운트
  4. 활성 예약이 있거나 노쇼가 2개 이상이면 신청 차단
- **재신청 허용 조건**: 노쇼 1개만 있는 경우 재신청 가능
- **실패 시**: `DUPLICATE_WAITING` 예외 발생

#### 5. 다음 대기 번호 생성
```java
// WaitingService.java:68-69
Integer nextWaitingNumber = waitingPort.getNextWaitingNumber(request.popupId());
```
- **목적**: 해당 팝업의 다음 대기 순번을 계산
- **로직**: 현재 WAITING 상태인 최대 대기번호 + 1
- **빈 대기열 처리**: 아무도 대기하지 않으면 0번 할당

#### 6. 회원 정보 확인
```java
// WaitingService.java:71-73
Member member = memberPort.findById(request.memberId())
        .orElseThrow(() -> new BusinessException(ErrorType.MEMBER_NOT_FOUND, String.valueOf(request.memberId())));
```
- **목적**: 신청자의 회원 정보가 존재하는지 확인
- **실패 시**: `MEMBER_NOT_FOUND` 예외 발생

#### 7. 예상 대기 시간 계산
```java
// WaitingService.java:75-76
Integer expectedWaitingTime = waitingStatisticsPort.findCompletedStatisticsByPopupId(request.popupId())
        .calculateExpectedWaitingTime(nextWaitingNumber);
```
- **목적**: 통계 기반으로 예상 대기 시간 제공
- **계산 방식**: 과거 완료된 대기 통계를 기반으로 현재 대기번호에 따른 예상 시간 산출
- **활용**: 사용자에게 대기 시간 정보 제공 (UX 개선)

#### 8. 대기 정보 생성
```java
// WaitingService.java:78-92
Waiting waiting = new Waiting(
        null, // ID는 저장소에서 생성
        popup,
        request.name(),
        member,
        request.contactEmail(),
        request.peopleCount(),
        nextWaitingNumber,
        WaitingStatus.WAITING,
        now,
        null,
        null,
        expectedWaitingTime
);
```
- **목적**: 검증된 정보로 Waiting 도메인 객체 생성
- **초기 상태**: `WaitingStatus.WAITING`
- **등록 시간**: 현재 시간으로 자동 설정
- **예상 대기 시간**: 계산된 예상 시간 포함

#### 9. 대기 정보 저장
```java
// WaitingService.java:95-96
Waiting savedWaiting = waitingPort.save(waiting);
```
- **목적**: 생성된 대기 정보를 데이터베이스에 영속화
- **트랜잭션**: `@Transactional` 범위에서 처리

#### 10. 알림 처리
```java
// WaitingService.java:98-99
waitingNotificationService.processWaitingCreatedNotifications(savedWaiting);
```
- **목적**: 대기 신청 완료 알림 및 향후 알림 스케줄링
- **처리 내용**: 즉시 알림 발송 + 미래 알림 예약

#### 11. 응답 생성
```java
// WaitingService.java:101-102
return waitingDtoMapper.toCreateResponse(savedWaiting);
```
- **목적**: 클라이언트에게 반환할 응답 DTO 생성
- **포함 정보**: 대기 ID, 대기 번호, 예상 대기 시간 등

## 입력 데이터 검증

### 1. 요청 DTO 구조
```java
// WaitingCreateRequest.java:6-13
public record WaitingCreateRequest(
    Long popupId,       // 팝업 ID
    Long memberId,      // 회원 ID  
    String name,        // 대기자 이름
    Integer peopleCount, // 대기 인원수
    String contactEmail // 연락처 이메일
)
```

### 2. 도메인 레벨 검증 (Waiting 생성자)

#### 2.1 대기 인원수 검증
```java
// Waiting.java:47-48
if (peopleCount == null || peopleCount < 1 || peopleCount > 6) {
    throw new BusinessException(ErrorType.INVALID_PEOPLE_COUNT, String.valueOf(peopleCount));
}
```
- **제약조건**: 1명 이상 6명 이하
- **실패 시**: `INVALID_PEOPLE_COUNT` 예외

#### 2.2 대기자 이름 검증
```java
// Waiting.java:50-55
if (waitingPersonName == null || waitingPersonName.length() < 2 || waitingPersonName.length() > 20) {
    throw new BusinessException(ErrorType.INVALID_WAITING_PERSON_NAME, waitingPersonName);
}
if (!NAME_PATTERN.matcher(waitingPersonName).matches()) {
    throw new BusinessException(ErrorType.INVALID_WAITING_PERSON_NAME, waitingPersonName);
}
```
- **길이 제약**: 2자 이상 20자 이하
- **문자 제약**: 한글, 영문, 숫자만 허용 (`^[a-zA-Z가-힣0-9][a-zA-Z가-힣0-9]*$`)
- **실패 시**: `INVALID_WAITING_PERSON_NAME` 예외

#### 2.3 이메일 검증
```java
// Waiting.java:21-22
@Email(message = "대기자 이메일이 형식에 맞지 않습니다.")
String contactEmail
```
- **제약조건**: 표준 이메일 형식 (Jakarta Bean Validation)
- **실패 시**: Validation 예외

## 대기 신청 실패 시나리오

### 1. 비즈니스 규칙 위반

#### 1.1 팝업이 운영 중이 아님 (POPUP_NOT_OPENED)
```java
ErrorType.POPUP_NOT_OPENED (HttpStatus.BAD_REQUEST, "팝업이 운영 중이 아닙니다")
```
- **발생 시점**: 팝업 운영 확인 단계
- **원인**: 현재 시간이 팝업 운영 시간 범위를 벗어남
- **HTTP 상태**: 400 Bad Request

#### 1.2 제재된 회원 (BANNED_MEMBER)
```java
ErrorType.BANNED_MEMBER (HttpStatus.FORBIDDEN, "제재된 회원입니다")
```
- **발생 시점**: 제재 여부 확인 단계
- **원인**: 팝업별 제재 또는 전체 제재 대상 회원
- **HTTP 상태**: 403 Forbidden
- **참고**: [제재 시스템 비즈니스 로직](ban-system-business-logic.md) 참조

#### 1.3 존재하지 않는 팝업 (POPUP_NOT_FOUND)
```java
ErrorType.POPUP_NOT_FOUND (HttpStatus.NOT_FOUND, "팝업을 찾을 수 없습니다")
```
- **발생 시점**: 팝업 존재 여부 확인 단계
- **원인**: 잘못된 popupId 또는 삭제된 팝업
- **HTTP 상태**: 404 Not Found

#### 1.4 중복 신청 (DUPLICATE_WAITING)
```java
ErrorType.DUPLICATE_WAITING (HttpStatus.BAD_REQUEST, "이미 대기 했던 팝업입니다")
```
- **발생 시점**: 중복 신청 확인 단계
- **원인**: 당일 동일 회원이 같은 팝업에 이미 신청
- **HTTP 상태**: 400 Bad Request
- **검증 범위**: 당일 기준 (날짜별 재신청 가능)

#### 1.5 존재하지 않는 회원 (MEMBER_NOT_FOUND)
```java
ErrorType.MEMBER_NOT_FOUND (HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다")
```
- **발생 시점**: 회원 정보 확인 단계
- **원인**: 잘못된 memberId 또는 삭제된 회원
- **HTTP 상태**: 404 Not Found

### 2. 입력 데이터 검증 실패

#### 2.1 유효하지 않은 대기 인원수 (INVALID_PEOPLE_COUNT)
```java
ErrorType.INVALID_PEOPLE_COUNT (HttpStatus.BAD_REQUEST, "대기 인원수가 유효하지 않습니다")
```
- **발생 시점**: Waiting 도메인 객체 생성 시
- **원인**: null, 1 미만, 6 초과 인원수
- **HTTP 상태**: 400 Bad Request

#### 2.2 유효하지 않은 대기자 이름 (INVALID_WAITING_PERSON_NAME)
```java
ErrorType.INVALID_WAITING_PERSON_NAME (HttpStatus.BAD_REQUEST, "대기자 이름이 유효하지 않습니다")
```
- **발생 시점**: Waiting 도메인 객체 생성 시
- **원인 1**: 길이 제약 위반 (2자 미만, 20자 초과)
- **원인 2**: 문자 제약 위반 (한글, 영문, 숫자 외 문자 포함)
- **HTTP 상태**: 400 Bad Request

#### 2.3 유효하지 않은 이메일 형식
```java
// Jakarta Bean Validation 예외
message: "대기자 이메일이 형식에 맞지 않습니다."
```
- **발생 시점**: Bean Validation 처리 시
- **원인**: 표준 이메일 형식에 맞지 않는 입력
- **HTTP 상태**: 400 Bad Request

### 3. 시스템 오류

#### 3.1 데이터베이스 저장 실패
- **발생 시점**: `waitingPort.save()` 호출 시
- **원인**: DB 연결 오류, 제약조건 위반, 트랜잭션 롤백 등
- **처리**: RuntimeException으로 전파, 트랜잭션 롤백

#### 3.2 알림 처리 실패
- **발생 시점**: `waitingNotificationService.processWaitingCreatedNotifications()` 호출 시
- **원인**: 알림 서버 오류, 네트워크 오류 등
- **처리**: 로깅 후 계속 진행 (대기 신청 자체는 성공)

## 성공 조건

대기 신청이 성공하려면 다음 조건들이 모두 만족되어야 합니다:

### 1. 필수 데이터 존재
- ✅ 유효한 팝업 ID (존재하는 팝업)
- ✅ 유효한 회원 ID (존재하는 회원)
- ✅ 유효한 대기자 이름 (2-20자, 한글/영문/숫자만)
- ✅ 유효한 대기 인원수 (1-6명)
- ✅ 유효한 이메일 주소 (표준 이메일 형식)

### 2. 비즈니스 규칙 준수
- ✅ 팝업이 운영 중인 상태
- ✅ 제재되지 않은 회원 (팝업별/전체 제재 모두)
- ✅ 당일 해당 팝업에 중복 신청하지 않음

### 3. 시스템 정상 동작
- ✅ 데이터베이스 정상 동작
- ✅ 트랜잭션 성공적 처리
- ✅ 대기번호 정상 생성

## 응답 구조

### 성공 시 응답 (WaitingCreateResponse)
```java
{
  "waitingId": 123,
  "popupName": "팝업스토어명",
  "waitingPersonName": "홍길동",
  "peopleCount": 2,
  "contactEmail": "hong@example.com",
  "waitingNumber": 5,
  "registeredAt": "2025-01-12T14:30:00",
  "expectedWaitingTime": 30,  // 예상 대기 시간 (분)
  "location": {
    "addressName": "서울시 강남구...",
    "region1DepthName": "서울시",
    // ... 위치 정보
  },
  "mainImageUrl": "https://..."
}
```

### 실패 시 응답 (ErrorResponse)
```java
{
  "code": "DUPLICATE_WAITING",
  "message": "이미 대기 했던 팝업입니다",
  "additionalInfo": "1",  // popupId
  "timestamp": "2025-01-12T14:30:00",
  "path": "/api/waitings"
}
```


## 연관 기능

### 1. 대기 입장 처리
- 대기번호 0인 경우에만 입장 가능
- 입장 시 다른 대기자들의 번호 자동 감소

### 2. 알림 시스템 연동
- 신청 완료 즉시 알림 발송
- 입장 시간 임박 알림 예약
- 입장 가능 알림 예약

### 3. 대기 이력 관리
- 신청/입장/취소 상태 추적
- 방문 이력 조회 기능 제공

이 비즈니스 로직을 통해 시스템은 안정적이고 사용자 친화적인 대기 신청 서비스를 제공할 수 있습니다.