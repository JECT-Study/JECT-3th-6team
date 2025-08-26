package com.example.demo.domain.model.popup;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * 특정 요일의 운영 시간을 나타내는 값 객체.
 *
 * @param dayOfWeek 요일
 * @param openTime  오픈 시간
 * @param closeTime 마감 시간
 */
public record OpeningHours(
        DayOfWeek dayOfWeek,
        LocalTime openTime,
        LocalTime closeTime
) {
} 