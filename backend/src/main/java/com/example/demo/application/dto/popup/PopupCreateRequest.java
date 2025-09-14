package com.example.demo.application.dto.popup;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 팝업 생성 요청 DTO
 */
public record PopupCreateRequest(
        String name,
        String type, // PopupType enum name (e.g., EXPERIENTIAL, EXHIBITION, RETAIL)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

        LocationCreate location,

        List<OpeningHoursCreate> weeklyOpeningHours,

        // 기존 imageUrls를 두 개로 분리
        List<String> mainImageUrls,        // 메인 이미지들 (필수)
        List<String> brandStoryImageUrls,  // 브랜드 스토리 이미지들 (필수)

        ContentCreate content,

        List<SnsCreate> sns,

        List<Long> categoryIds
) {
    public record LocationCreate(
            String addressName,
            String region1DepthName,
            String region2DepthName,
            String region3DepthName,
            Double longitude,
            Double latitude
    ) {}

    public record OpeningHoursCreate(
            String dayOfWeek, // java.time.DayOfWeek name
            String openTime,  // HH:mm
            String closeTime  // HH:mm
    ) {}

    public record ContentCreate(
            String introduction,
            String notice
    ) {}

    public record SnsCreate(
            String iconUrl,
            String linkUrl
    ) {}
}


