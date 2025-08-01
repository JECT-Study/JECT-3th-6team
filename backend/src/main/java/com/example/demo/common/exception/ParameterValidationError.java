package com.example.demo.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParameterValidationError implements ValidationError {
    
    private final String parameterName;
    private final Object rejectedValue;
    private final String reason;
    
    public static ParameterValidationError of(String parameterName, Object rejectedValue, String reason) {
        return new ParameterValidationError(parameterName, rejectedValue, reason);
    }
}