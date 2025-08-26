package com.example.demo.domain.model;

/**
 * 위치 정보 도메인 모델.
 * 주소와 좌표(longitude, latitude)를 포함한다.
 */
public record Location(
    String addressName,
    String region1depthName,
    String region2depthName,
    String region3depthName,
    double longitude,
    double latitude
) {
    public Location {
        if (region3depthName == null) {
            region3depthName = "";
        }
    }
}