package com.example.demo.domain.model;

import java.time.LocalDate;

public record Period(
    LocalDate startDate,
    LocalDate endDate
) {}
