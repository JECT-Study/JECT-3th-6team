package com.example.demo.domain.model.popup;

import com.example.demo.domain.model.DateRange;

/**
 * 팝업의 일정 정보를 나타내는 값 객체.
 *
 * @param dateRange          전체 운영 기간
 * @param weeklyOpeningHours 주간 상세 운영 시간
 */
public record PopupSchedule(
    DateRange dateRange,
    WeeklyOpeningHours weeklyOpeningHours
) {
} 