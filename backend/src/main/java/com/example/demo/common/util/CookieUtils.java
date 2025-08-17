package com.example.demo.common.util;

import org.springframework.http.ResponseCookie;

public class CookieUtils {

    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

    /**
     * 액세스 토큰 쿠키 생성
     * SameSite=Lax; HttpOnly 설정으로 보안과 호환성을 모두 고려
     */
    public static ResponseCookie createAccessTokenCookie(String token, long maxAgeSeconds) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, token)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAgeSeconds)
                .domain(".spotit.co.kr")
                .secure(true)
                .sameSite("None")
                .build();
    }

    /**
     * 쿠키 삭제용
     */
    public static ResponseCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .domain(".spotit.co.kr")
                .secure(true)
                .sameSite("None")
                .build();
    }
} 
