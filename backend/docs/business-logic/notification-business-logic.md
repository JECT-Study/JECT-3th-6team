# 알림 시스템 비즈니스 로직

## 개요

알림 시스템은 웨이팅 관련 이벤트를 사용자에게 실시간으로 전달하는 기능을 제공합니다. 전송 방식은 **즉시 전송**과 **스케줄러 기반 조건부 전송**으로 구분되며, 전달 채널은 **SSE(Server-Sent Events)**와 **이메일**을 지원합니다.

## 알림 전송 방식

### 1. 즉시 전송 알림

웨이팅 생성 시 즉시 발송되는 알림으로, 사용자 액션에 대한 즉각적인 피드백을 제공합니다.

**트리거**: 웨이팅 생성 완료 시점
**처리 서비스**: `WaitingNotificationService.processWaitingCreatedNotifications()`

#### 비즈니스 규칙
- **알림 타입**: `WAITING_CONFIRMED`
- **발송 시점**: 웨이팅 생성 트랜잭션 완료 직후
- **내용 규칙**: `"MM.dd (요일) N인 웨이팅이 완료되었습니다. 현재 대기 번호를 확인해주세요!"`
- **전달 채널**: SSE만 사용 (이메일 미발송)

#### 처리 플로우
1. `WaitingService.createWaiting()` → 웨이팅 생성
2. `WaitingNotificationService.processWaitingCreatedNotifications()` 호출
3. `sendWaitingConfirmedNotification()` → 즉시 알림 생성 및 발송
4. `createScheduledNotifications()` → 미래 알림 스케줄링

```java
// 즉시 알림 발송 로직 (WaitingNotificationService.java:61-84)
private void sendWaitingConfirmedNotification(Waiting waiting) {
    // 1. 알림 콘텐츠 생성
    String content = generateWaitingConfirmedContent(waiting);
    
    // 2. 도메인 이벤트와 알림 객체 생성
    WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.WAITING_CONFIRMED);
    Notification notification = Notification.builder()
            .member(waiting.member())
            .event(event)
            .content(content)
            .build();
    
    // 3. 알림 저장 (영속화)
    Notification savedNotification = notificationPort.save(notification);
    
    // 4. SSE 연결이 있는 경우에만 실시간 발송
    if (notificationEventPort.isConnected(memberId)) {
        notificationEventPort.sendRealTimeNotification(memberId, savedNotification);
    }
}
```

### 2. 스케줄러 기반 조건부 전송

미리 정의된 트리거 조건이 만족될 때 배치로 처리되는 알림입니다.

**트리거**: 30초마다 실행되는 배치 스케줄러
**처리 서비스**: `ScheduledNotificationBatchService.processScheduledNotifications()`

#### 지원 트리거 타입

##### 2.1 입장 시작 알림 (WAITING_ENTER_NOW)
**조건**: 현재 웨이팅 번호가 0번 (바로 입장 가능한 상태)
**내용**: `"지금 매장으로 입장 부탁드립니다. 즐거운 시간 보내세요!"`
**전달 채널**: SSE + 이메일

```java
// 트리거 조건 검사 (ScheduledNotificationBatchService.java:84-93)
private boolean checkEnterNowTrigger(ScheduledNotification scheduledNotification) {
    Waiting waiting = extractWaitingFromNotification(scheduledNotification);
    return waiting != null && 
           waiting.waitingNumber() == 0 && 
           waiting.status() == WaitingStatus.WAITING;
}
```

##### 2.2 입장 3팀 전 알림 (WAITING_ENTER_3TEAMS_BEFORE)
**조건**: 현재 웨이팅 번호가 3번 (앞에 3팀 대기)
**내용**: `"앞으로 3팀 남았습니다! 순서가 다가오니 매장 근처에서 대기해주세요!"`
**전달 채널**: SSE만 사용

```java
// 트리거 조건 검사 (ScheduledNotificationBatchService.java:117-138)
private boolean checkEnter3TeamsBeforeTrigger(ScheduledNotification scheduledNotification) {
    Waiting waiting = extractWaitingFromNotification(scheduledNotification);
    if (waiting == null) return false;
    
    int currentPosition = calculateCurrentWaitingPosition(waiting);
    return currentPosition == 3; // 4번째 순번 (0부터 시작)
}
```

##### 2.3 입장 시간 초과 알림 (WAITING_ENTER_TIME_OVER)
**조건**: 현재 비활성화됨
**상태**: 구현되어 있지만 트리거 조건에서 `false` 반환
**내용**: `"입장 시간이 초과되었습니다. 빠른 입장 부탁드립니다!"`

#### 스케줄러 처리 플로우
1. **배치 실행**: 30초마다 `@Scheduled(fixedDelay = 30_000)` 실행
2. **대상 조회**: 미처리 `ScheduledNotification` 목록 조회
3. **조건 검사**: 각 알림의 트리거 조건 검사
4. **알림 발송**: 조건 만족 시 알림 생성 및 발송
5. **정리**: 발송 완료된 스케줄 삭제

```java
// 배치 처리 메인 로직 (ScheduledNotificationBatchService.java:41-65)
@Scheduled(fixedDelay = 30_000)
public void processScheduledNotifications() {
    List<ScheduledNotification> pendingNotifications = getPendingScheduledNotifications();
    
    List<ScheduledNotification> needToSend = pendingNotifications.stream()
            .filter(this::isTriggerConditionSatisfied)
            .toList();
    
    List<ScheduledNotification> completeSend = needToSend.stream()
            .map(this::sendScheduledNotification)
            .filter(Objects::nonNull)
            .toList();
    
    scheduledNotificationPort.delete(completeSend);
}
```

## 알림 전달 채널

### 1. SSE (Server-Sent Events) 전달

실시간 알림을 위한 브라우저 기반 전달 방식입니다.

**구현체**: `NotificationSseAdapter`
**연결 관리**: 회원별 다중 연결 지원
**타임아웃**: 30분
**하트비트**: 30초마다 ping 전송

#### SSE 연결 관리 로직
```java
// 연결 관리 구조 (NotificationSseAdapter.java:29-30)
private final Map<Long, Map<String, SseEmitter>> memberConnections = new ConcurrentHashMap<>();

// 실시간 알림 발송 (NotificationSseAdapter.java:35-42)
public void sendRealTimeNotification(Long memberId, Notification notification) {
    Optional.ofNullable(memberConnections.get(memberId))
            .filter(connections -> !connections.isEmpty())
            .ifPresentOrElse(
                    connections -> sendNotificationToConnections(memberId, notification, connections),
                    () -> log.debug("회원에 대한 SSE 연결이 없습니다. 실시간 알림을 스킵합니다.")
            );
}
```

#### SSE 이벤트 타입
- **connected**: 연결 성공 확인
- **notification**: 실제 알림 데이터
- **ping**: 하트비트 (연결 유지)

#### 연결 상태 관리
- 자동 재연결 지원
- 오류/타임아웃 시 자동 정리
- 다중 탭/기기 연결 지원

### 2. 이메일 전달

입장 알림에 대한 이메일 통지를 제공합니다.

**구현체**: `GmailSmtpEmailAdapter`
**발송 조건**: `WAITING_ENTER_NOW` 트리거만
**비동기 처리**: `@Async` 어노테이션 사용
**템플릿**: HTML 형식

#### 이메일 발송 플로우
1. **트리거**: 입장 시작 알림 발송 시
2. **정보 수집**: 웨이팅/팝업/위치 정보 추출
3. **템플릿 생성**: `EmailTemplateService.buildWaitingEntryTemplate()`
4. **비동기 발송**: `EmailNotificationService.sendWaitingEntryNotificationAsync()`

```java
// 이메일 발송 로직 (ScheduledNotificationBatchService.java:210-238)
private void sendEntryEmailNotification(Waiting waiting) {
    var popup = waiting.popup();
    var location = popup.getLocation();
    
    // 카카오맵 링크 생성
    String storeLocation = generateMapLink(location);
    
    var request = new WaitingEntryNotificationRequest(
            popup.getName(),
            waiting.waitingPersonName(),
            waiting.peopleCount(),
            waiting.contactEmail(),
            waiting.registeredAt(),
            storeLocation
    );
    
    // 비동기 발송
    emailNotificationService.sendWaitingEntryNotificationAsync(request);
}
```

#### 이메일 콘텐츠 구성
- **제목**: `"[매장명] 지금 입장해주세요!"`
- **본문**: HTML 템플릿 기반
- **포함 정보**: 
  - 대기자 정보 (이름, 인원수, 대기일시)
  - 매장 정보 (이름, 위치)
  - 카카오맵 링크

## 비즈니스 정책 상수

### 시간 관련 정책
```java
// WaitingNotificationService.java:38
private static final int AVERAGE_WAITING_TIME_MINUTES = 15; // 팀당 평균 대기 시간

// NotificationSseAdapter.java:32
private static final long SSE_TIMEOUT = 30 * 60 * 1000L; // 30분 SSE 타임아웃
```

### 스케줄러 정책
```java
// 알림 배치 처리: 30초마다
@Scheduled(fixedDelay = 30_000)

// SSE 하트비트: 30초마다  
@Scheduled(fixedRateString = "${app.sse.heartbeat.ping-interval:30000}")

// 웨이팅 자동 입장: 5분마다 (임시 로직)
@Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
```

### 알림 내용 정책
```java
// 웨이팅 확정 알림 (WaitingNotificationService.java:173-175)
return String.format("%s (%s) %d인 웨이팅이 완료되었습니다. 현재 대기 번호를 확인해주세요!",
        dateText, dayText, peopleCount);

// 입장 시작 알림 (WaitingNotificationService.java:115)
"지금 매장으로 입장 부탁드립니다. 즐거운 시간 보내세요!"

// 3팀 전 알림 (WaitingNotificationService.java:145)
"앞으로 3팀 남았습니다! 순서가 다가오니 매장 근처에서 대기해주세요!"
```

## 알림 도메인 모델

### 핵심 엔티티
- **Notification**: 실제 알림 데이터 (영속화됨)
- **ScheduledNotification**: 미래 알림 예약 (조건 만족 시 Notification으로 변환)
- **DomainEvent**: 알림을 발생시킨 도메인 이벤트 정보
- **WaitingDomainEvent**: 웨이팅 관련 도메인 이벤트

### 알림 상태 관리
- **NotificationStatus**: 알림 상태 (활성/비활성 등)
- **ReadStatus**: 읽음 상태 (읽음/안읽음)
- **ScheduledNotificationTrigger**: 스케줄 트리거 타입

## 예외 상황 처리

### SSE 연결 오류
- IOException 발생 시 해당 연결 자동 제거
- 모든 연결 실패 시 회원 정보 정리
- 재연결 시 새로운 connectionId로 등록

### 이메일 발송 오류
- 비동기 처리로 메인 로직에 영향 없음
- 실패 시 로그 기록 및 예외 전파
- 재시도 로직 없음 (단발성 알림)

### 스케줄러 오류
- 개별 알림 처리 실패가 전체 배치에 영향 없음
- 트랜잭션 분리로 부분 실패 허용
- 실패한 스케줄은 삭제되지 않아 재시도 가능

이 비즈니스 로직을 통해 사용자는 웨이팅 상황에 대한 적시적소의 알림을 받을 수 있으며, 시스템은 확장 가능하고 신뢰할 수 있는 알림 서비스를 제공합니다.