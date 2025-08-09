# 웨이팅 입장 알림 이메일 기능

## 개요

웨이팅 입장 알림 이메일을 발송하는 기능입니다. 이미지에 표시된 형식과 동일한 HTML 템플릿으로 이메일을 발송합니다.

## 필요한 정보

이메일 발송을 위해 다음 정보가 필요합니다:

- `storeName`: 스토어명
- `memberName`: 대기자명 (고객 이름)
- `waitingCount`: 대기자 수
- `memberEmail`: 대기자 이메일
- `waitingDateTime`: 대기 일자 (LocalDateTime)
- `storeLocation`: 매장 위치 (URL 또는 주소)

## 사용법

### 1. DTO 생성

```java
WaitingEntryNotificationRequest request = new WaitingEntryNotificationRequest(
    "스타벅스 강남점",           // 스토어명
    "이윤재",                   // 대기자명
    3,                         // 대기자 수
    "lki3532@naver.com",      // 대기자 이메일
    LocalDateTime.now(),       // 대기 일자
    "https://maps.google.com"  // 매장 위치
);
```

### 2. 이메일 발송

#### 동기 발송

```java
emailNotificationService.sendWaitingEntryNotification(request);
```

#### 비동기 발송

```java
emailNotificationService.sendWaitingEntryNotificationAsync(request);
```

### 3. API 테스트

#### 동기 발송 테스트

```bash
POST /api/email/waiting-entry
Content-Type: application/json

{
  "storeName": "스타벅스 강남점",
  "memberName": "이윤재",
  "waitingCount": 3,
  "memberEmail": "lki3532@naver.com",
  "waitingDateTime": "2025-06-26T13:00:00",
  "storeLocation": "https://maps.google.com"
}
```

#### 비동기 발송 테스트

```bash
POST /api/email/waiting-entry/async
Content-Type: application/json

{
  "storeName": "스타벅스 강남점",
  "memberName": "이윤재",
  "waitingCount": 3,
  "memberEmail": "lki3532@naver.com",
  "waitingDateTime": "2025-06-26T13:00:00",
  "storeLocation": "https://maps.google.com"
}
```

## 이메일 템플릿 특징

1. **반응형 디자인**: 모바일 친화적인 레이아웃
2. **스팟잇 브랜딩**: 로고와 색상이 포함된 브랜드 일관성
3. **명확한 정보 표시**: 대기 정보를 체계적으로 정리
4. **행동 유도**: 입장 시간 제한과 매장 위치 안내
5. **고객 서비스**: 문의 방법과 피드백 채널 제공

## 주의사항

- `waitingDateTime`은 ISO 8601 형식으로 전송해야 합니다 (예: "2025-06-26T13:00:00")
- `storeLocation`은 클릭 가능한 URL이어야 합니다
- 이메일 발송은 비동기로 처리하는 것을 권장합니다 (성능상 이점)
