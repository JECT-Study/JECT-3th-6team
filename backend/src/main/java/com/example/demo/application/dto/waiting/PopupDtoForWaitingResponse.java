package com.example.demo.application.dto.waiting;

import com.example.demo.application.dto.popup.LocationResponse;

// 다른 클래스에서 활용시 합의 필요
public record PopupDtoForWaitingResponse(
        Long popupId,
        String popupName,
        String popupImageUrl,
        LocationResponse location,
        long dDay,
        String period
) {
}
