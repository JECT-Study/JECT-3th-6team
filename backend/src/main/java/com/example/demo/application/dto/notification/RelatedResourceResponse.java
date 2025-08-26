package com.example.demo.application.dto.notification;

import java.util.Map;

public record RelatedResourceResponse(
        String type,
        Map<String, Object> data
) {
} 