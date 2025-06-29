package com.example.demo.domain.model;

import java.util.List;

public record Rating(
    double averageStar,
    int reviewCount
) {
    public static Rating from(List<Integer> ratings) {
        double avg = ratings.isEmpty() ? 0.0 :
            Math.round(ratings.stream().mapToInt(r -> r).average().orElse(0.0) * 10.0) / 10.0;
        return new Rating(avg, ratings.size());
    }
}