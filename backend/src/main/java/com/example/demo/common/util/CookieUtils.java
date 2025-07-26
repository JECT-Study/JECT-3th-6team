package com.example.demo.common.util;

import jakarta.servlet.http.Cookie;

public class CookieUtils {

    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

    public static Cookie createAccessTokenCookie(String token, long maxAgeSeconds) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAgeSeconds);
        return cookie;
    }
} 