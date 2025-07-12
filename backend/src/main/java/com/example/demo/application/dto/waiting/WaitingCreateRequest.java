package com.example.demo.application.dto.waiting;

/**
 * 현장 대기 신청 요청 DTO (popupId 포함)
 */
public record WaitingCreateRequest(
    Long popupId,
    Long memberId,
    String name,
    Integer peopleCount,
    String contactEmail
) {
} 
