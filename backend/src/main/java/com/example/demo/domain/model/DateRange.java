package com.example.demo.domain.model;

import java.time.LocalDate;

/**
 * 날짜 범위 도메인 모델.
 * 시작일과 종료일을 나타낸다.
 */
public record DateRange(
    LocalDate startDate,
    LocalDate endDate
) {
} 