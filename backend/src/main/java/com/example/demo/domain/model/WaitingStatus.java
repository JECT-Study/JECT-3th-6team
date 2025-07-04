package com.example.demo.domain.model;

/**
 * 대기 상태 enum.
 * 대기의 현재 상태를 나타낸다.
 */
public enum WaitingStatus {
    RESERVED,    // 예약/대기중
    COMPLETED,   // 방문완료
    CANCELED;    // 취소됨
    
    /**
     * 문자열을 WaitingStatus로 파싱한다.
     * 
     * @param status 상태 문자열
     * @return WaitingStatus (null이면 null 반환)
     * @throws IllegalArgumentException 유효하지 않은 상태인 경우
     */
    public static WaitingStatus fromString(String status) {
        if (status == null) {
            return null;
        }
        try {
            return valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 상태입니다: " + status);
        }
    }
} 