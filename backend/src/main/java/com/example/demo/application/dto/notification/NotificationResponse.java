package com.example.demo.application.dto.notification;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long notificationId,
        String notificationCode,
        String message,
        LocalDateTime createdAt,
        boolean isRead,
        RelatedResourceResponse relatedResource
) {
} 