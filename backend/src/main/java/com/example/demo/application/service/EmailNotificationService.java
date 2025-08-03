package com.example.demo.application.service;

import com.example.demo.domain.model.email.EmailMessage;
import com.example.demo.domain.port.EmailSendPort;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 이메일 알림 발송 유스케이스.
 * <p>
 *  - 현재는 웨이팅 입장 알림 템플릿만 제공한다.
 */
@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final EmailSendPort emailSendPort;

    /**
     * 웨이팅 입장 알림 메일을 전송한다.
     *
     * @param storeName  팝업 또는 매장명
     * @param memberName 수신자 이름
     * @param toEmail    수신자 이메일
     */
    public void sendWaitingEnterEmail(String storeName, String memberName, String toEmail) {
        String subject = String.format("[%s] 입장 알림", storeName);
        String body = buildWaitingEnterBody(storeName, memberName);

        EmailMessage message = new EmailMessage(toEmail, subject, body);
        emailSendPort.sendEmail(message);
    }

    @Async
    public CompletableFuture<Void> sendWaitingEnterEmailAsync(String storeName, String memberName, String toEmail) {
        sendWaitingEnterEmail(storeName, memberName, toEmail);
        return CompletableFuture.completedFuture(null);
    }

    private String buildWaitingEnterBody(String storeName, String memberName) {
        return "안녕하세요, " + memberName + "님!<br/><br/>" +
                "스팟잇을 이용해 주셔서 진심으로 감사합니다.<br/><br/>" +
                "웨이팅하고 계셨던 <b>" + storeName + " 스토어</b>의 입장 순서가 되었습니다! 지금 바로 입장해주세요!<br/><br/>" +
                "빠른 입장 부탁드리며, 다음에도 스팟잇과 함께 특별한 순간을 놓치지 마세요!<br/><br/>" +
                "감사합니다.<br/><br/>" +
                "지금, 이 순간의 핫플을, spot it!<br/>" +
                "<i>스팟잇</i><br/><br/>" +
                "<ul style=\"margin:0; padding-left:20px;\">" +
                "<li>질문이 있으신가요? 0spotit0@gmail.com</li>" +
                "<li>스팟잇에게 의견을 남겨주세요! (링크)</li>" +
                "<li>스팟잇이 더 궁금하다면? (사이트 링크)</li>" +
                "</ul>";
    }
}
