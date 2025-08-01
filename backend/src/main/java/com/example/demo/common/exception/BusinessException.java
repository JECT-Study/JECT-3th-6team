package com.example.demo.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    
    private final ErrorType errorType;
    private final String additionalInfo;
    
    public BusinessException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.additionalInfo = null;
    }
    
    public BusinessException(ErrorType errorType, String additionalInfo) {
        super(errorType.getMessage() + " - " + additionalInfo);
        this.errorType = errorType;
        this.additionalInfo = additionalInfo;
    }
    
    public BusinessException(ErrorType errorType, Throwable cause) {
        super(errorType.getMessage(), cause);
        this.errorType = errorType;
        this.additionalInfo = null;
    }
    
    public BusinessException(ErrorType errorType, String additionalInfo, Throwable cause) {
        super(errorType.getMessage() + " - " + additionalInfo, cause);
        this.errorType = errorType;
        this.additionalInfo = additionalInfo;
    }
}