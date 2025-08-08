package com.example.demo.application.dto.popup;

import java.util.List;

/**
 * 팝업 생성 응답 DTO
 */
public record PopupCreateResponse(
        Long popupId,
        String name,
        String type,
        List<String> imageUrls
) {}


