package com.example.demo.application.dto.popup;

public record LocationResponse(
    String addressName,
    String region1depthName,
    String region2depthName,
    String region3depthName,
    double longitude,
    double latitude
) {
}
