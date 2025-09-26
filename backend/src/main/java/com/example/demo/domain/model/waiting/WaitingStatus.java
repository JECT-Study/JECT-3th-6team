package com.example.demo.domain.model.waiting;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;

/**
 * 대기 상태 enum.
 * 대기의 현재 상태를 나타낸다.
 */
public enum WaitingStatus {
    WAITING,    // 예약/대기중
    VISITED,   // 방문완료
    CANCELED,  // 취소됨 (사용 안함)
    NONE,      // 사용 안함
    NO_SHOW;   // 노쇼

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
            throw new BusinessException(ErrorType.INVALID_WAITING_STATUS, status);
        }
    }
} 