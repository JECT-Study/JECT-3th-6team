package com.example.demo.common.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ValidationErrorResponse {
    
    private final String code;
    private final String message;
    private final List<ValidationErrorDetail> errors;
    private final LocalDateTime timestamp;
    private final String path;
    
    @Getter
    @Builder
    public static class ValidationErrorDetail {
        private final String parameterName;
        private final Object rejectedValue;
        private final String reason;
        
        public static ValidationErrorDetail from(ValidationError validationError) {
            return ValidationErrorDetail.builder()
                    .parameterName(validationError.getParameterName())
                    .rejectedValue(validationError.getRejectedValue())
                    .reason(validationError.getReason())
                    .build();
        }
    }
    
    public static ValidationErrorResponse of(ParameterValidationException exception, String path) {
        List<ValidationErrorDetail> errorDetails = exception.getValidationErrors().stream()
                .map(ValidationErrorDetail::from)
                .toList();
        
        return ValidationErrorResponse.builder()
                .code("PARAMETER_VALIDATION_FAILED")
                .message("요청 파라미터 검증에 실패했습니다")
                .errors(errorDetails)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }
}