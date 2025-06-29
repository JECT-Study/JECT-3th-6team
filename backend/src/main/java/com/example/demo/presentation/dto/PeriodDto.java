package com.example.demo.presentation.dto;

import com.example.demo.domain.model.Period;

public record PeriodDto(
    String startDate,
    String endDate
) {
    public static PeriodDto from(Period period) {
        return new PeriodDto(
            period.startDate().toString(),
            period.endDate().toString()
        );
    }
}
