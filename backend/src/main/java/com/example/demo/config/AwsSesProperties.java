package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AWS SES 관련 설정 값을 주입받는 프로퍼티 클래스.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "aws.ses")
public class AwsSesProperties {
    /** AWS IAM Access Key */
    private String accessKey;
    /** AWS IAM Secret Key */
    private String secretKey;
    /** SES가 동작하는 AWS Region */
    private String region;
    /** 발신자 이메일 (SES에서 인증된 주소) */
    private String sourceEmail;
}
