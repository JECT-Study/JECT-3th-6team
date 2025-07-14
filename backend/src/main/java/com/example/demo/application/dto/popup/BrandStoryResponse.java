package com.example.demo.application.dto.popup;

import java.util.List;

public record BrandStoryResponse(
    List<String> imageUrls,
    List<SnsResponse> sns
) {}