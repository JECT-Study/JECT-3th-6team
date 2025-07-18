package com.example.demo.application.dto.notification;

public record NotificationListRequest(
        Long memberId,
        Integer size,
        Long lastNotificationId,
        String readStatus,
        String sort
) {
} 