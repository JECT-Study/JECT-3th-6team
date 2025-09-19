package com.example.demo.domain.model.waiting;


/**
 * 대기 상태 enum.
 * 대기의 현재 상태를 나타낸다.
 */
public enum WaitingStatus {
    WAITING,    // 예약/대기중
    VISITED,   // 방문완료
    CANCELED,  // 취소됨
    NONE;

    /**
     * 문자열을 WaitingStatus로 파싱한다.
     *
     * @param status 상태 문자열
     * @return WaitingStatus (null이면 null 반환)
     */
    public static WaitingStatus fromString(String status) {
        if (status == null) {
            return null;
        }
        try {
            return valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
} 