package com.example.demo.domain.model;

import java.time.Duration;
import java.time.LocalDate;

public record Period(
    LocalDate startDate,
    LocalDate endDate
) {
    public int dDay() {
        int days = (int) Duration.between(
            LocalDate.now().atStartOfDay(),
            endDate.atStartOfDay()
        ).toDays();
        return Math.max(days, 0);
    }
}
