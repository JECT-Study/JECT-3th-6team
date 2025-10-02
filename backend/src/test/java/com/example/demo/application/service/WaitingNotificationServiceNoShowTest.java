// package com.example.demo.application.service;

// import com.example.demo.application.dto.notification.WaitingEntryNotificationRequest;
// import com.example.demo.domain.model.Member;
// import com.example.demo.domain.model.popup.Popup;
// import com.example.demo.domain.model.waiting.Waiting;
// import com.example.demo.domain.port.NotificationEventPort;
// import com.example.demo.domain.port.NotificationPort;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Nested;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.LocalDateTime;

// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class WaitingNotificationServiceNoShowTest {

//     @Mock
//     private NotificationPort notificationPort;

//     @Mock
//     private NotificationEventPort notificationEventPort;

//     @Mock
//     private EmailNotificationService emailNotificationService;

//     @InjectMocks
//     private WaitingNotificationService waitingNotificationService;

//     private Waiting waiting;
//     private Member member;
//     private Popup popup;

//     @BeforeEach
//     void setUp() {
//         member = new Member(1L, "테스트회원", "test@example.com");
//         popup = Popup.builder()
//                 .id(1L)
//                 .name("테스트팝업")
//                 .build();
        
//         waiting = new Waiting(
//                 1L, popup, "테스트대기자", member, "test@example.com",
//                 2, 1, com.example.demo.domain.model.waiting.WaitingStatus.WAITING, 
//                 LocalDateTime.now(), null, null
//         );
//     }

//     @Nested
//     @DisplayName("노쇼 알림 처리 테스트")
//     class ProcessNoShowNotificationsTest {

//         @Test
//         @DisplayName("첫 번째 노쇼 알림을 발송한다")
//         void shouldSendFirstNoShowNotification() {
//             // given
//             long noShowCount = 1L;
//             when(notificationPort.save(any())).thenReturn(com.example.demo.domain.model.notification.Notification.builder().build());
//             when(notificationEventPort.isConnected(anyLong())).thenReturn(true);

//             // when
//             waitingNotificationService.processNoShowNotifications(waiting, noShowCount);

//             // then
//             verify(notificationPort).save(any());
//             verify(notificationEventPort).sendRealTimeNotification(anyLong(), any());
//         }

//         @Test
//         @DisplayName("두 번째 노쇼 알림을 발송한다")
//         void shouldSendSecondNoShowNotification() {
//             // given
//             long noShowCount = 2L;
//             when(notificationPort.save(any())).thenReturn(com.example.demo.domain.model.notification.Notification.builder().build());
//             when(notificationEventPort.isConnected(anyLong())).thenReturn(true);

//             // when
//             waitingNotificationService.processNoShowNotifications(waiting, noShowCount);

//             // then
//             verify(notificationPort).save(any());
//             verify(notificationEventPort).sendRealTimeNotification(anyLong(), any());
//         }
//     }

//     @Nested
//     @DisplayName("글로벌 밴 알림 테스트")
//     class GlobalBanNotificationTest {

//         @Test
//         @DisplayName("글로벌 밴 알림을 발송한다")
//         void shouldSendGlobalBanNotification() {
//             // given
//             Long memberId = 1L;
//             when(notificationPort.save(any())).thenReturn(com.example.demo.domain.model.notification.Notification.builder().build());
//             when(notificationEventPort.isConnected(anyLong())).thenReturn(true);

//             // when
//             waitingNotificationService.sendGlobalBanNotification(memberId);

//             // then
//             verify(notificationPort).save(any());
//             verify(notificationEventPort).sendRealTimeNotification(anyLong(), any());
//         }
//     }

//     @Nested
//     @DisplayName("입장 알림 테스트")
//     class EnterNotificationTest {

//         @Test
//         @DisplayName("새로운 0번에게 입장 알림을 발송한다")
//         void shouldSendEnterNowNotification() {
//             // given
//             when(notificationPort.save(any())).thenReturn(com.example.demo.domain.model.notification.Notification.builder().build());
//             when(notificationEventPort.isConnected(anyLong())).thenReturn(true);

//             // when
//             waitingNotificationService.sendEnterNowNotification(waiting);

//             // then
//             verify(notificationPort).save(any());
//             verify(notificationEventPort).sendRealTimeNotification(anyLong(), any());
//             verify(emailNotificationService).sendWaitingEntryNotificationAsync(any(WaitingEntryNotificationRequest.class));
//         }

//         @Test
//         @DisplayName("3팀 전 알림을 발송한다")
//         void shouldSendEnter3TeamsBeforeNotification() {
//             // given
//             when(notificationPort.save(any())).thenReturn(com.example.demo.domain.model.notification.Notification.builder().build());
//             when(notificationEventPort.isConnected(anyLong())).thenReturn(true);

//             // when
//             waitingNotificationService.sendEnter3TeamsBeforeNotification(waiting);

//             // then
//             verify(notificationPort).save(any());
//             verify(notificationEventPort).sendRealTimeNotification(anyLong(), any());
//         }
//     }

//     @Nested
//     @DisplayName("SSE 연결 테스트")
//     class SseConnectionTest {

//         @Test
//         @DisplayName("SSE 연결이 없는 경우 실시간 알림을 스킵한다")
//         void shouldSkipRealTimeNotificationWhenSseNotConnected() {
//             // given
//             when(notificationPort.save(any())).thenReturn(com.example.demo.domain.model.notification.Notification.builder().build());
//             when(notificationEventPort.isConnected(anyLong())).thenReturn(false);

//             // when
//             waitingNotificationService.sendEnterNowNotification(waiting);

//             // then
//             verify(notificationPort).save(any());
//             verify(notificationEventPort, never()).sendRealTimeNotification(anyLong(), any());
//         }

//         @Test
//         @DisplayName("SSE 연결이 있는 경우 실시간 알림을 발송한다")
//         void shouldSendRealTimeNotificationWhenSseConnected() {
//             // given
//             when(notificationPort.save(any())).thenReturn(com.example.demo.domain.model.notification.Notification.builder().build());
//             when(notificationEventPort.isConnected(anyLong())).thenReturn(true);

//             // when
//             waitingNotificationService.sendEnterNowNotification(waiting);

//             // then
//             verify(notificationPort).save(any());
//             verify(notificationEventPort).sendRealTimeNotification(anyLong(), any());
//         }
//     }
// }
