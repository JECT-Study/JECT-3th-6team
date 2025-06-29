package com.example.demo.application.dto;

import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.Period;
import com.example.demo.domain.model.PopupDetail;
import com.example.demo.domain.model.PopupDetailInfo;
import com.example.demo.domain.model.Rating;
import com.example.demo.domain.model.SearchTags;
import java.util.List;

public record PopupDetailDto(
    Long id,
    String title,
    List<String> thumbnails,
    int dDay,
    Rating rating,
    SearchTags searchTags,
    Location location,
    Period period,
    BrandStory brandStory,
    PopupDetailInfo popupDetail
) {
    public static PopupDetailDto fromDomain(PopupDetail popup) {
        return new PopupDetailDto(
            popup.id(),
            popup.title(),
            popup.thumbnails(),
            popup.dDay(),
            popup.rating(),
            popup.searchTags(),
            popup.location(),
            popup.period(),
            popup.brandStory(),
            popup.popupDetail()
        );
    }
}