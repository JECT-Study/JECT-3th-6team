package com.example.demo.presentation.controller;

import com.example.demo.application.dto.waiting.VisitHistoryCursorResponse;
import com.example.demo.application.dto.waiting.WaitingCreateRequest;
import com.example.demo.application.dto.waiting.WaitingCreateResponse;
import com.example.demo.application.service.WaitingService;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.presentation.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "대기 관리", description = "팝업스토어 대기 및 방문 내역 관리 API")
public class WaitingController {

    private final WaitingService waitingService;

    @PostMapping("/popups/{popupId}/waitings")
    @Operation(summary = "대기 신청", description = "특정 팝업스토어에 대기를 신청합니다.")
    public ResponseEntity<ApiResponse<WaitingCreateResponse>> createWaiting(
            @Parameter(description = "팝업 ID") @PathVariable Long popupId,
            @Parameter(description = "대기 신청 정보") @RequestBody WaitingCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        WaitingCreateRequest createRequest = new WaitingCreateRequest(
                popupId,
                principal.getId(),
                request.name(),
                request.peopleCount(),
                request.contactEmail()
        );
        WaitingCreateResponse response = waitingService.createWaiting(createRequest, LocalDateTime.now());

        return ResponseEntity.ok(new ApiResponse<>("성공적으로 대기가 등록되었습니다.", response));
    }

    @GetMapping("/me/visits")
    @Operation(summary = "방문 내역 조회", description = "내 방문/예약 내역을 조회합니다. 무한 스크롤 또는 단건 조회가 가능합니다.")
    public ResponseEntity<ApiResponse<VisitHistoryCursorResponse>> getVisitHistory(
            @Parameter(description = "조회할 개수 (기본값: 10)") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "마지막 대기 ID (무한 스크롤용)") @RequestParam(required = false) Long lastWaitingId,
            @Parameter(description = "상태 필터") @RequestParam(required = false) String status,
            @Parameter(description = "특정 대기 ID (단건 조회용)") @RequestParam(required = false) Long waitingId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        VisitHistoryCursorResponse response = waitingService.getVisitHistory(principal.getId(), size, lastWaitingId, status, waitingId);

        return ResponseEntity.ok(new ApiResponse<>("성공적으로 조회되었습니다.", response));
    }
} 
