package com.example.demo.application.dto.popup;

import java.math.BigDecimal;

/**
 * 지도에 표시될 팝업의 간소화된 정보를 담는 응답 DTO 입니다.
 *
 * @param id        팝업의 고유 식별자
 * @param latitude  위도
 * @param longitude 경도
 */
public record PopupMapResponse(
        Long id,
        BigDecimal latitude,
        BigDecimal longitude
) {
} 