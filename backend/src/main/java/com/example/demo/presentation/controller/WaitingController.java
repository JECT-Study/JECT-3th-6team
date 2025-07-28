package com.example.demo.presentation.controller;

import com.example.demo.application.dto.waiting.VisitHistoryCursorResponse;
import com.example.demo.application.dto.waiting.WaitingCreateRequest;
import com.example.demo.application.dto.waiting.WaitingCreateResponse;
import com.example.demo.application.service.WaitingService;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService waitingService;

    /**
     * 현장 대기 신청
     */
    @PostMapping("/popups/{popupId}/waitings")
    public ResponseEntity<ApiResponse<WaitingCreateResponse>> createWaiting(
            @PathVariable Long popupId,
            @RequestBody WaitingCreateRequest request
    ) {
        WaitingCreateRequest createRequest = new WaitingCreateRequest(
                popupId,
                request.memberId(),
                request.name(),
                request.peopleCount(),
                request.contactEmail()
        );
        WaitingCreateResponse response = waitingService.createWaiting(createRequest);

        return ResponseEntity.ok(new ApiResponse<>("성공적으로 대기가 등록되었습니다.",response));
    }

    /**
     * 내 방문/예약 내역 조회 (무한 스크롤) 또는 단건 조회
     */
    @GetMapping("/me/visits")
    public ResponseEntity<ApiResponse<VisitHistoryCursorResponse>> getVisitHistory(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastWaitingId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long waitingId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        VisitHistoryCursorResponse response = waitingService.getVisitHistory(principal.getId(), size, lastWaitingId, status, waitingId);

        return ResponseEntity.ok(new ApiResponse<>("성공적으로 조회되었습니다.", response));
    }
} 
