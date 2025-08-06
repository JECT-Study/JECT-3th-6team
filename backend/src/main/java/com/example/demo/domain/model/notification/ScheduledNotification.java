package com.example.demo.domain.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ScheduledNotification {
    private final Long id;
    private final Notification reservatedNotification; // 예약 생성할 알림
    private final ScheduledNotificationTrigger scheduledNotificationTrigger; // 실제 생성될 트리거 규칙
    private final LocalDateTime enterNotificationSentAt; // 입장 알림 발송 시간
    private final LocalDateTime actualEnterTime; // 실제 입장 시간

    public ScheduledNotification(Notification notification, ScheduledNotificationTrigger scheduledNotificationTrigger) {
        this(null, notification, scheduledNotificationTrigger, null, null);
    }

    public ScheduledNotification(Long id, Notification notification, ScheduledNotificationTrigger scheduledNotificationTrigger) {
        this(id, notification, scheduledNotificationTrigger, null, null);
    }
}
