package com.example.demo.infrastructure.external;

import com.example.demo.config.GmailSmtpProperties;
import com.example.demo.domain.model.email.EmailMessage;
import com.example.demo.domain.port.EmailSendPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

/**
 * Gmail SMTP를 사용해 이메일을 전송하는 어댑터.
 * EmailSendPort의 구현체로 인프라스트럭처 계층에 위치한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GmailSmtpEmailAdapter implements EmailSendPort {

    private final JavaMailSender javaMailSender;
    private final GmailSmtpProperties properties;

    @Override
    public void sendEmail(EmailMessage email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            
            // 발신자 설정 (표시 이름 <커스텀 도메인 이메일> 형식)
            String fromAddress = String.format("%s <%s>", properties.getFromName(), properties.getFromEmail());
            helper.setFrom(fromAddress);
            
            helper.setTo(email.to());
            helper.setSubject(email.subject());
            helper.setText(email.body(), true); // HTML 형식으로 발송
            
            javaMailSender.send(mimeMessage);
            log.info("Gmail SMTP 이메일 전송 완료. 수신자: {}", email.to());
            
        } catch (Exception e) {
            log.error("Gmail SMTP 이메일 전송 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이메일 전송에 실패했습니다.", e);
        }
    }
} 