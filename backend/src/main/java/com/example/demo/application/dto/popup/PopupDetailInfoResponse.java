package com.example.demo.application.dto.popup;

import java.util.List;

public record PopupDetailInfoResponse(
    List<DayOfWeekInfoResponse> dayOfWeeks,
    List<String> descriptions
) {
}
