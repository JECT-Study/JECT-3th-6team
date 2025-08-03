package com.example.demo.common.security;

import com.example.demo.common.exception.ErrorResponse;
import com.example.demo.common.exception.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.warn("Authentication failed: {}, path: {}", authException.getMessage(), request.getRequestURI());

        // JWT 필터에서 설정한 에러 정보 확인
        String jwtError = (String) request.getAttribute("JWT_ERROR");
        ErrorType errorType = determineErrorType(jwtError);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    private ErrorType determineErrorType(String jwtError) {
        if ("EXPIRED".equals(jwtError)) {
            return ErrorType.EXPIRED_TOKEN;
        } else if ("INVALID".equals(jwtError)) {
            return ErrorType.INVALID_TOKEN;
        } else {
            return ErrorType.AUTHENTICATION_REQUIRED;
        }
    }
}