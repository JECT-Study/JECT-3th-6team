package com.example.demo.application.dto;

import com.example.demo.application.dto.popup.*;

import java.util.List;

public record PopupDetailResponse(
        Long id,
        List<String> thumbnails,
        int dDay,
        String title,
        SearchTagsResponse searchTags,
        LocationResponse location,
        PeriodResponse period,
        BrandStoryResponse brandStory,
        PopupDetailInfoResponse popupDetail,
        WaitingStatusForPopupDetailResponse status
) {

    public enum WaitingStatusForPopupDetailResponse {
        WAITING,    // 예약/대기중
        VISITED,   // 방문완료
        NONE,      // 사용 안함
        NO_SHOW,   // 노쇼
        STORE_BAN,
        GLOBAL_BAN;
    }
}