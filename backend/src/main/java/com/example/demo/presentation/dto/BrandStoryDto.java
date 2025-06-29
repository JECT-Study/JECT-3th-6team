package com.example.demo.presentation.dto;

import com.example.demo.domain.model.BrandStory;
import java.util.List;

public record BrandStoryDto(
    List<String> imageUrls,
    List<SnsDto> sns
) {
    public static BrandStoryDto from(BrandStory brandStory) {
        return new BrandStoryDto(
            brandStory.imageUrls(),
            brandStory.sns().stream()
                .map(SnsDto::from)
                .toList()
        );
    }
}