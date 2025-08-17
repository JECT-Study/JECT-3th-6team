package com.example.demo.domain.model.popup;

import com.example.demo.domain.model.DateRange;

import java.math.BigDecimal;
import java.util.List;

/**
 * 지도 기반 팝업 검색을 위한 쿼리 객체입니다.
 *
 * @param minLatitude  최소 위도
 * @param maxLatitude  최대 위도
 * @param minLongitude 최소 경도
 * @param maxLongitude 최대 경도
 * @param types        팝업 유형
 * @param categories   카테고리 목록
 * @param dateRange    검색 기간
 */
public record PopupMapQuery(
        BigDecimal minLatitude,
        BigDecimal maxLatitude,
        BigDecimal minLongitude,
        BigDecimal maxLongitude,
        List<PopupType> types,
        List<String> categories,
        DateRange dateRange
) {
} 