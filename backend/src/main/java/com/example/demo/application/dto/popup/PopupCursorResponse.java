package com.example.demo.application.dto.popup;

import java.util.List;

public record PopupCursorResponse(
        List<PopupListElementResponse> content,
        Long lastPopupId,
        Boolean hasNext
) {
    public record PopupListElementResponse(
            Long popupId,
            String popupName,
            String popupImageUrl,
            LocationResponse location,
            long dDay,
            String period,
            SearchTagsResponse searchTags,
            int waitingCount
    ) {
    }
}