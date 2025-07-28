package com.example.demo.application.dto.notification;

import java.util.List;

public record NotificationListResponse(
        List<NotificationResponse> content,
        Long lastNotificationId,
        boolean hasNext
) {
} 