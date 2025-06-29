package com.example.demo.domain.model;

import java.util.List;

public record PopupDetail(
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
) {}
