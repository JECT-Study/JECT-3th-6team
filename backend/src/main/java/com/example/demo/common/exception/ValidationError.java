package com.example.demo.common.exception;

public interface ValidationError {
    
    /**
     * 검증에 실패한 파라미터의 이름을 반환합니다.
     */
    String getParameterName();
    
    /**
     * 검증에 실패한 값을 반환합니다.
     */
    Object getRejectedValue();
    
    /**
     * 검증 실패 이유를 반환합니다.
     */
    String getReason();
}