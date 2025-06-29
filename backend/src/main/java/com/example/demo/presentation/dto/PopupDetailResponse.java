package com.example.demo.presentation.dto;

import com.example.demo.application.dto.PopupDetailDto;
import java.util.List;

public record PopupDetailResponse(
    Long id,
    String title,
    List<String> thumbnails,
    int dDay,
    RatingDto rating,
    SearchTagsDto searchTags,
    LocationDto location,
    PeriodDto period,
    BrandStoryDto brandStory,
    PopupDetailInfoDto popupDetail
) {
    public static PopupDetailResponse fromApplicationDto(PopupDetailDto popup) {
        return new PopupDetailResponse(
            popup.id(),
            popup.title(),
            popup.thumbnails(),
            popup.dDay(),
            RatingDto.from(popup.rating()),
            SearchTagsDto.from(popup.searchTags()),
            LocationDto.from(popup.location()),
            PeriodDto.from(popup.period()),
            BrandStoryDto.from(popup.brandStory()),
            PopupDetailInfoDto.from(popup.popupDetail())
        );
    }
}
