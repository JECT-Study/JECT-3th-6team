package com.example.demo.presentation.controller;

import com.example.demo.application.dto.notification.*;
import com.example.demo.application.service.NotificationService;
import com.example.demo.application.service.NotificationSseService;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.presentation.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "알림 관리", description = "알림 조회, SSE 스트림, 읽기 처리 등 알림 관련 API")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationSseService notificationSseService;

    @GetMapping
    @Operation(summary = "알림 목록 조회", description = "사용자의 알림 목록을 조회합니다. 커서 기반 페이징을 지원합니다.")
    public ApiResponse<NotificationListResponse> getNotifications(
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "알림 조회 요청 (커서, 크기, 정렬 등)") NotificationListRequest request
    ) {
        NotificationListRequest requestWithMemberId = new NotificationListRequest(
                principal.getId(),
                request.size(),
                request.lastNotificationId(),
                request.readStatus(),
                request.sort()
        );

        NotificationListResponse response = notificationService.getNotifications(requestWithMemberId);
        return new ApiResponse<>("알림 목록 조회 성공", response);
    }

    @GetMapping(value = "/stream", produces = "text/event-stream")
    @Operation(summary = "알림 SSE 스트림", description = "실시간 알림을 받기 위한 Server-Sent Events 연결을 생성합니다.")
    public SseEmitter streamNotifications(@AuthenticationPrincipal UserPrincipal principal) {
        return notificationSseService.createSseConnection(principal.getId());
    }

    @GetMapping("/connection-status")
    @Operation(summary = "SSE 연결 상태 조회", description = "현재 SSE 연결 상태를 확인합니다.")
    public ApiResponse<Boolean> getConnectionStatus(@AuthenticationPrincipal UserPrincipal principal) {
        boolean isConnected = notificationSseService.isConnected(principal.getId());
        return new ApiResponse<>("연결 상태 조회 성공", isConnected);
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 상태로 변경합니다.")
    public ApiResponse<NotificationResponse> markNotificationAsRead(
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "알림 ID") @PathVariable Long notificationId
    ) {
        NotificationResponse notificationResponse = notificationService.markNotificationAsRead(principal.getId(), new NotificationReadRequest(notificationId));

        return new ApiResponse<>("알림 읽기 성공", notificationResponse);
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "알림 삭제", description = "특정 알림을 삭제합니다.")
    public ApiResponse<Void> deleteNotification(
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "알림 ID") @PathVariable Long notificationId
    ) {
        notificationService.deleteNotification(principal.getId(), new NotificationDeleteRequest(notificationId));

        return new ApiResponse<>("알림 삭제 성공", null);
    }
}
