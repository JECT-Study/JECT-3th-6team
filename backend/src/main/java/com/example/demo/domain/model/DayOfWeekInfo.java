package com.example.demo.domain.model;

import com.example.demo.infrastructure.persistence.entity.popup.PopupWeeklyScheduleEntity;
import java.time.format.TextStyle;
import java.util.Locale;

public record DayOfWeekInfo(
    String dayOfWeek,
    String value
) {
    public static DayOfWeekInfo from(PopupWeeklyScheduleEntity sch) {
        return new DayOfWeekInfo(
            sch.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase(),
            sch.getOpenTime() + " ~ " + sch.getCloseTime()
        );
    }
}