package com.example.demo.domain.model.popup;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;

/**
 * 팝업 유형을 나타내는 열거형.
 * 체험형, 전시형, 판매형 중 하나를 나타낸다.
 */
public enum PopupType {
    /**
     * 체험형
     */
    EXPERIENTIAL("체험형"),
    /**
     * 전시형
     */
    EXHIBITION("전시형"),
    /**
     * 판매형
     */
    RETAIL("판매형");

    private final String korean;

    PopupType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

    public static PopupType fromKorean(String value) {
        for (PopupType type : values()) {
            if (type.korean.equals(value)) {
                return type;
            }
        }
        throw new BusinessException(ErrorType.INVALID_POPUP_TYPE, value);
    }
} 