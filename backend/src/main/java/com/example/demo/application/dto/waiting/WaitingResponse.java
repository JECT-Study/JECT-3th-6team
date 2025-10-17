package com.example.demo.application.dto.waiting;

import com.example.demo.application.dto.popup.PopupSummaryResponse;

import java.time.LocalDateTime;

/**
 * 방문 내역 조회에서 사용할 Waiting 객체 응답 DTO
 */
public record WaitingResponse(
        Long waitingId,
        Integer waitingNumber, // 대기 번호
        String status, // 예약 상태
        String name, // 예약자 이름
        int peopleCount, // 예약 인원
        String contactEmail, // 예약 이메일
        PopupSummaryResponse popup,
        LocalDateTime registeredAt,
        Integer expectedWaitingTimeMinutes, // 예상 대기 시간 (분)
        Integer waitingCount // 해당 팝업의 대기중인 팀 수
) {
}
