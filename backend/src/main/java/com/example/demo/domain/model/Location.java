package com.example.demo.domain.model;

public record Location(
    String addressName,
    String region1depthName,
    String region2depthName,
    String region3depthName,
    double longitude,
    double latitude
) {}