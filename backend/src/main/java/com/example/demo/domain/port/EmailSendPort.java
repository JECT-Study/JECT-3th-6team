package com.example.demo.domain.port;

import com.example.demo.domain.model.email.EmailMessage;

/**
 * 이메일 발송을 위한 아웃고잉 포트.
 * 구현체는 인프라스트럭처 계층에서 AWS SES 등 외부 시스템을 사용해 메일을 전송한다.
 */
public interface EmailSendPort {

    /**
     * 이메일을 전송한다.
     *
     * @param email 전송할 이메일 메시지
     */
    void sendEmail(EmailMessage email);
}
