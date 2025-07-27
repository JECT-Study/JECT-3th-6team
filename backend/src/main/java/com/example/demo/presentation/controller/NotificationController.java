package com.example.demo.presentation.controller;

import com.example.demo.application.dto.notification.NotificationListRequest;
import com.example.demo.application.dto.notification.NotificationListResponse;
import com.example.demo.application.service.NotificationService;
import com.example.demo.application.service.NotificationSseService;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationSseService notificationSseService;

    /**
     * 알림 내역 조회
     *
     * @param size               한 번에 조회할 항목 개수 (기본값: 20, 최대값: 100)
     * @param lastNotificationId 이전 조회 결과의 마지막 notificationId (첫 페이지 조회 시 생략)
     * @param readStatus         조회할 알림 상태 (READ, UNREAD, ALL - 기본값: ALL)
     * @param sort               정렬 기준 (UNREAD_FIRST, TIME_DESC - 기본값: TIME_DESC)
     * @return 알림 목록 응답
     */
    @GetMapping
    public ApiResponse<NotificationListResponse> getNotifications(
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) Long lastNotificationId,
            @RequestParam(required = false, defaultValue = "ALL") String readStatus,
            @RequestParam(required = false, defaultValue = "TIME_DESC") String sort,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        NotificationListRequest request = new NotificationListRequest(
                principal.getId(),
                size,
                lastNotificationId,
                readStatus,
                sort
        );

        NotificationListResponse response = notificationService.getNotifications(request);

        return new ApiResponse<>("성공", response);
    }

    /**
     * SSE를 통한 실시간 알림 연결
     *
     * @param principal 인증된 사용자 정보
     * @return SSE Emitter
     */
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter streamNotifications(@AuthenticationPrincipal UserPrincipal principal) {
        return notificationSseService.createSseConnection(principal.getId());
    }

    /**
     * 회원의 SSE 연결 상태 확인
     *
     * @param principal 인증된 사용자 정보
     * @return 연결 상태 응답
     */
    @GetMapping("/connection-status")
    public ApiResponse<Boolean> getConnectionStatus(@AuthenticationPrincipal UserPrincipal principal) {
        boolean isConnected = notificationSseService.isConnected(principal.getId());
        return new ApiResponse<>("연결 상태 조회 성공", isConnected);
    }
}
