package com.example.demo.application.dto.popup;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 지도 내 팝업 조회를 위한 요청 데이터를 담는 DTO 입니다.
 *
 * @param minLatitude  최소 위도
 * @param maxLatitude  최대 위도
 * @param minLongitude 최소 경도
 * @param maxLongitude 최대 경도
 * @param type         팝업 유형
 * @param category     팝업 카테고리 (콤마로 구분)
 * @param startDate    검색 시작 기간
 * @param endDate      검색 종료 기간
 */
public record PopupMapRequest(
        @NotNull
        @DecimalMin(value = "-90.0", message = "위도는 -90도 이상이어야 합니다")
        @DecimalMax(value = "90.0", message = "위도는 90도 이하여야 합니다")
        BigDecimal minLatitude,
        @NotNull
        @DecimalMin(value = "-90.0", message = "위도는 -90도 이상이어야 합니다")
        @DecimalMax(value = "90.0", message = "위도는 90도 이하여야 합니다")
        BigDecimal maxLatitude,
        @NotNull
        @DecimalMin(value = "-180.0", message = "경도는 -180도 이상이어야 합니다")
        @DecimalMax(value = "180.0", message = "경도는 180도 이하여야 합니다")
        BigDecimal minLongitude,
        @NotNull
        @DecimalMin(value = "-180.0", message = "경도는 -180도 이상이어야 합니다")
        @DecimalMax(value = "180.0", message = "경도는 180도 이하여야 합니다")
        BigDecimal maxLongitude,
        String type,
        String category,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate
) {
} 