package com.example.demo.presentation.dto;

import com.example.demo.domain.model.Rating;

public record RatingDto(
    double averageStar,
    int reviewCount
) {
    public static RatingDto from(Rating rating) {
        return new RatingDto(
            rating.averageStar(),
            rating.reviewCount()
        );
    }
}
