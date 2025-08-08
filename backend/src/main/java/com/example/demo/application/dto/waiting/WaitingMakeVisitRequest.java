package com.example.demo.application.dto.waiting;

/**
 * 대기열 입장 처리 요청 DTO
 */
public record WaitingMakeVisitRequest(
        Long waitingId
) {
}