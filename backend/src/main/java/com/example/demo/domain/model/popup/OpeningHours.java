package com.example.demo.domain.model.popup;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
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
    public OpeningHours {
        if (openTime.isAfter(closeTime)) {
            throw new BusinessException(ErrorType.INVALID_OPENING_HOURS, String.format("오픈시간: %s, 마감시간: %s", openTime, closeTime));
        }
    }
} 