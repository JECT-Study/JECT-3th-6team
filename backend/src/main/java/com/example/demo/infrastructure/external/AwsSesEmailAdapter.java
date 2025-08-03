package com.example.demo.infrastructure.external;

import com.example.demo.config.AwsSesProperties;
import com.example.demo.domain.model.email.EmailMessage;
import com.example.demo.domain.port.EmailSendPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

/**
 * AWS SES를 사용해 이메일을 전송하는 어댑터.
 * EmailSendPort의 구현체로 인프라스트럭처 계층에 위치한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AwsSesEmailAdapter implements EmailSendPort {

    private final SesClient sesClient;
    private final AwsSesProperties properties;
    private static final String DEFAULT_CHARSET = "UTF-8";

    @Override
    public void sendEmail(EmailMessage email) {
        SendEmailRequest request = SendEmailRequest.builder()
                .source(properties.getSourceEmail())
                .destination(d -> d.toAddresses(email.to()))
                .message(msg -> msg
                        .subject(sub -> sub.data(email.subject()).charset(DEFAULT_CHARSET))
                        .body(b -> b.html(content -> content.data(email.body()).charset(DEFAULT_CHARSET)))
                )
                .build();

        try {
            SendEmailResponse response = sesClient.sendEmail(request);
            log.info("SES 이메일 전송 완료. MessageId: {}", response.messageId());
        } catch (software.amazon.awssdk.services.ses.model.SesException e) {
            log.error("SES 이메일 전송 실패: {}", e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
}
