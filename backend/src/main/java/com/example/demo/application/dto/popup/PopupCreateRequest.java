package com.example.demo.application.dto.popup;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 팝업 생성 요청 DTO
 */
public record PopupCreateRequest(
        @NotBlank String name,
        @NotBlank String type, // PopupType enum name (e.g., EXPERIENTIAL, EXHIBITION, RETAIL)
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

        @NotNull @Valid LocationCreate location,

        @NotNull @Size(min = 1) List<@Valid OpeningHoursCreate> weeklyOpeningHours,

        @NotNull @Size(min = 1) List<@NotBlank String> imageUrls,

        @NotNull @Valid ContentCreate content,

        @NotNull List<@Valid SnsCreate> sns,

        @NotNull @Size(min = 1) List<@NotNull Long> categoryIds
) {
    public record LocationCreate(
            @NotBlank String addressName,
            @NotBlank String region1DepthName,
            @NotBlank String region2DepthName,
            String region3DepthName,
            @NotNull Double longitude,
            @NotNull Double latitude
    ) {}

    public record OpeningHoursCreate(
            @NotBlank String dayOfWeek, // java.time.DayOfWeek name
            @NotBlank String openTime,  // HH:mm
            @NotBlank String closeTime  // HH:mm
    ) {}

    public record ContentCreate(
            @NotBlank String introduction,
            @NotBlank String notice
    ) {}

    public record SnsCreate(
            @NotBlank String iconUrl,
            @NotBlank String linkUrl
    ) {}
}


