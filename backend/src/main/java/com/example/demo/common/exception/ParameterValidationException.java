package com.example.demo.common.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ParameterValidationException extends RuntimeException {

    private final List<ValidationError> validationErrors;

    public ParameterValidationException(List<ValidationError> validationErrors) {
        super("파라미터 검증에 실패했습니다");
        this.validationErrors = validationErrors;
    }

    public ParameterValidationException(ValidationError validationError) {
        this(List.of(validationError));
    }
}