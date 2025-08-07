package com.example.demo.presentation.controller;

import com.example.demo.application.dto.notification.WaitingEntryNotificationRequest;
import com.example.demo.application.service.EmailNotificationService;
import com.example.demo.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 이메일 발송 테스트용 컨트롤러
 */
@RestController
@RequestMapping("/api/email-test")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailNotificationService emailNotificationService;

    /**
     * 웨이팅 입장 알림 이메일 테스트 발송
     */
    @PostMapping("/waiting-entry")
    public ApiResponse<Void> sendWaitingEntryNotification(@RequestBody WaitingEntryNotificationRequest request) {
        emailNotificationService.sendWaitingEntryNotificationAsync(request);
        return new ApiResponse<>("이메일이 성공적으로 발송되었습니다.", null);
    }
} 