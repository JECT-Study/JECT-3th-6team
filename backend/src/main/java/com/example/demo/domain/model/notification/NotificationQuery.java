package com.example.demo.domain.model.notification;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NotificationQuery {

    private final Long notificationId;
    private final Long memberId;
    private final Long lastNotificationId;
    private final ReadStatus readStatus;
    private final Integer pageSize;
    private final NotificationSortOrder sortOrder;

    @Builder
    private NotificationQuery(Long notificationId, Long memberId, Long lastNotificationId, ReadStatus readStatus, Integer pageSize, NotificationSortOrder sortOrder) {
        this.notificationId = notificationId;
        this.memberId = memberId;
        this.lastNotificationId = lastNotificationId;
        this.readStatus = readStatus;
        this.pageSize = pageSize;
        this.sortOrder = sortOrder;
    }

    public static NotificationQuery findByNotificationId(Long notificationId) {
        return NotificationQuery.builder()
                .notificationId(notificationId)
                .build();
    }
} 