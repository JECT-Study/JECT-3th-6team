package com.example.demo.domain.model;

import java.util.List;

public record BrandStory(
    List<String> imageUrls,
    List<Sns> sns
) {}
