package com.example.demo.presentation.dto;

import com.example.demo.domain.model.DayOfWeekInfo;

public record DayOfWeekInfoDto(
    String dayOfWeek,
    String value
) {
    public static DayOfWeekInfoDto from(DayOfWeekInfo info) {
        return new DayOfWeekInfoDto(
            info.dayOfWeek(),
            info.value()
        );
    }
}
