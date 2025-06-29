package com.example.demo.presentation.dto;

import com.example.demo.domain.model.PopupDetailInfo;
import java.util.List;

public record PopupDetailInfoDto(
    List<DayOfWeekInfoDto> dayOfWeeks,
    List<String> descriptions
) {
    public static PopupDetailInfoDto from(PopupDetailInfo info) {
        return new PopupDetailInfoDto(
            info.dayOfWeeks().stream()
                .map(DayOfWeekInfoDto::from)
                .toList(),
            info.descriptions()
        );
    }
}