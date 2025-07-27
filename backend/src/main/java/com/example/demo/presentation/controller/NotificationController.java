package com.example.demo.presentation.controller;

import com.example.demo.application.dto.notification.NotificationListRequest;
import com.example.demo.application.dto.notification.NotificationListResponse;
import com.example.demo.application.service.NotificationService;
import com.example.demo.application.service.NotificationSseService;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationSseService notificationSseService;

    /**
     * 사용자의 알림 목록을 조회한다.
     *
     * @param principal 인증된 사용자 정보
     * @param request   알림 조회 요청 (커서, 크기, 정렬 등)
     * @return 알림 목록 응답
     */
    @GetMapping
    public ApiResponse<NotificationListResponse> getNotifications(
            @AuthenticationPrincipal UserPrincipal principal,
            NotificationListRequest request) {

        NotificationListResponse response = notificationService.getNotifications(request);
        return new ApiResponse<>("알림 목록 조회 성공", response);
    }

    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter streamNotifications(@AuthenticationPrincipal UserPrincipal principal) {
        return notificationSseService.createSseConnection(principal.getId());
    }

    @Profile("sse-test")
    @GetMapping(value = "/stream-test", produces = "text/event-stream")
    public SseEmitter testStreamNotifications() {
        Long memberId = 1000L; // 테스트용으로 고정된 회원 ID
        return notificationSseService.createSseConnection(memberId);
    }

    @GetMapping("/connection-status")
    public ApiResponse<Boolean> getConnectionStatus(@AuthenticationPrincipal UserPrincipal principal) {
        boolean isConnected = notificationSseService.isConnected(principal.getId());
        return new ApiResponse<>("연결 상태 조회 성공", isConnected);
    }
}
