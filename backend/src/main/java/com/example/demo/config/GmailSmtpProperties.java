package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Gmail SMTP 관련 설정 값을 주입받는 프로퍼티 클래스.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.mail")
public class GmailSmtpProperties {
    /** Gmail 계정 이메일 */
    private String username;
    /** Gmail 앱 비밀번호 */
    private String password;
    /** 발신자 표시 이름 */
    private String fromName = "Spotit";
    /** 커스텀 도메인 발신자 이메일 */
    private String fromEmail = "notify@spotit.co.kr";
} 