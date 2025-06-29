package com.example.demo.domain.model;

import java.util.List;

public record PopupDetailInfo(
    List<DayOfWeekInfo> dayOfWeeks,
    List<String> descriptions
) {}
