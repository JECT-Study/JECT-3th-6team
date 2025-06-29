package com.example.demo.presentation.dto;

import com.example.demo.domain.model.Location;

public record LocationDto(
    String addressName,
    String region1depthName,
    String region2depthName,
    String region3depthName,
    double x,
    double y
) {
    public static LocationDto from(Location loc) {
        return new LocationDto(
            loc.addressName(),
            loc.region1depthName(),
            loc.region2depthName(),
            loc.region3depthName(),
            loc.longitude(),
            loc.latitude()
        );
    }
}