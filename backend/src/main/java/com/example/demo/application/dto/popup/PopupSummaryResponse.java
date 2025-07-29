package com.example.demo.application.dto.popup;

// 다른 클래스에서 활용시 합의 필요
public record PopupSummaryResponse(
        Long popupId,
        String popupName,
        String popupImageUrl,
        LocationResponse location,
        long dDay,
        String period,
        SearchTagsResponse searchTags
) {
}
