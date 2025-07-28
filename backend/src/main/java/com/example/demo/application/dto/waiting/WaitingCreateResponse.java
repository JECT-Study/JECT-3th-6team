package com.example.demo.application.dto.waiting;

import com.example.demo.application.dto.popup.LocationResponse;

import java.time.LocalDateTime;

/**
 * 현장 대기 신청 응답 DTO
 */
public record WaitingCreateResponse(
        Long waitingId,
        String popupName,
        String name,
        Integer peopleCount,
        String email,
        Integer waitingNumber,
        LocalDateTime registeredAt,
        LocationResponse location,
        String popupImageUrl
) {
}
