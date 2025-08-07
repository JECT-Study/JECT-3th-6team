package com.example.demo.application.service;

import com.example.demo.application.dto.notification.WaitingEntryNotificationRequest;
import com.example.demo.domain.model.email.EmailMessage;
import com.example.demo.domain.port.EmailSendPort;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 이메일 알림 발송 유스케이스.
 * <p>
 *  - 웨이팅 입장 알림 템플릿을 제공한다.
 */
@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final EmailSendPort emailSendPort;
    private final EmailTemplateService emailTemplateService;

    /**
     * 웨이팅 입장 알림 메일을 전송한다.
     *
     * @param request 입장 알림 요청 정보
     */
    public void sendWaitingEntryNotification(WaitingEntryNotificationRequest request) {
        String subject = String.format("[%s] 지금 입장해주세요!", request.storeName());
        String body = emailTemplateService.buildWaitingEntryTemplate(request);

        EmailMessage message = new EmailMessage(request.memberEmail(), subject, body);
        emailSendPort.sendEmail(message);
    }

    @Async
    public CompletableFuture<Void> sendWaitingEntryNotificationAsync(WaitingEntryNotificationRequest request) {
        sendWaitingEntryNotification(request);
        return CompletableFuture.completedFuture(null);
    }


}
