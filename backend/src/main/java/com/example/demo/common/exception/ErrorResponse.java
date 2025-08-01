package com.example.demo.common.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    
    private final String code;
    private final String message;
    private final String additionalInfo;
    private final LocalDateTime timestamp;
    private final String path;
    
    public static ErrorResponse of(ErrorType errorType, String path) {
        return ErrorResponse.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }
    
    public static ErrorResponse of(ErrorType errorType, String additionalInfo, String path) {
        return ErrorResponse.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .additionalInfo(additionalInfo)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }
    
    public static ErrorResponse of(BusinessException exception, String path) {
        return ErrorResponse.builder()
                .code(exception.getErrorType().getCode())
                .message(exception.getErrorType().getMessage())
                .additionalInfo(exception.getAdditionalInfo())
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }
}