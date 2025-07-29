package com.example.demo.application.dto.notification;

import java.time.LocalDateTime;
import java.util.List;

public record NotificationResponse(
        Long notificationId,
        String notificationCode,
        String message,
        LocalDateTime createdAt,
        boolean isRead,
        List<RelatedResourceResponse> relatedResources
) {
} 