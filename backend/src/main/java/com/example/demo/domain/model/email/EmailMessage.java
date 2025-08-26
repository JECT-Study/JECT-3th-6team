package com.example.demo.domain.model.email;

/**
 * 이메일 메시지를 표현하는 값 객체.
 * 도메인 계층에서만 사용되며 프레임워크에 의존하지 않는다.
 */
public record EmailMessage(
        String to,
        String subject,
        String body
) {
}
