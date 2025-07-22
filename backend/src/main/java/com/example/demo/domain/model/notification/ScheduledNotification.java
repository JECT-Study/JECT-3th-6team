package com.example.demo.domain.model.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduledNotification {
    private final Long id;
    private final Notification reservatedNotification; // 예약 생성할 알림
    private final ScheduledNotificationTrigger scheduledNotificationTrigger; // 실제 생성될 트리거 규칙

    public ScheduledNotification(Notification notification, ScheduledNotificationTrigger scheduledNotificationTrigger) {
        this(null, notification, scheduledNotificationTrigger);
    }
}
