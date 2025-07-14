package com.example.demo.application.dto.waiting;

import java.util.List;

/**
 * 방문 내역 조회 커서 페이지네이션 응답 DTO
 */
public record VisitHistoryCursorResponse(
    List<WaitingResponse> content,
    Long lastWaitingId,
    Boolean hasNext
) {} 
