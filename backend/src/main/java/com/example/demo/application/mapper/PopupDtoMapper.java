package com.example.demo.application.mapper;

import com.example.demo.application.dto.popup.*;
import com.example.demo.application.dto.popup.PopupMapRequest;
import com.example.demo.application.dto.popup.PopupMapResponse;
import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.DateRange;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.popup.*;
import com.example.demo.domain.model.popup.PopupType;
import com.example.demo.infrastructure.persistence.entity.popup.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PopupDtoMapper {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public PopupDetailInfoResponse toPopupDetailInfoResponse(Popup popup) {
        if (popup == null) {
            return new PopupDetailInfoResponse(Collections.emptyList(), Collections.emptyList());
        }

        List<DayOfWeekInfoResponse> dayOfWeekInfos = popup.getSchedule().weeklyOpeningHours().toList().stream()
                .map(this::toDayOfWeekInfoResponse)
                .collect(Collectors.toList());

        // PopupDetailInfoResponse가 SNS를 포함하지 않으므로, 여기서는 introduction과 notice만 description으로 변환합니다.
        List<String> descriptions = List.of(
                popup.getDisplay().content().introduction(),
                popup.getDisplay().content().notice()
        );

        return new PopupDetailInfoResponse(dayOfWeekInfos, descriptions);
    }

    public DayOfWeekInfoResponse toDayOfWeekInfoResponse(OpeningHours openingHours) {
        if (openingHours == null) return null;

        String openTime = openingHours.openTime() != null ? openingHours.openTime().format(TIME_FORMATTER) : "휴무";
        String closeTime = openingHours.closeTime() != null ? openingHours.closeTime().format(TIME_FORMATTER) : "";
        String value = openTime.equals("휴무") ? "휴무" : openTime + " ~ " + closeTime;

        return new DayOfWeekInfoResponse(openingHours.dayOfWeek().toString(), value);
    }

    public PeriodResponse toPeriodResponse(DateRange dateRange) {
        if (dateRange == null) return null;
        String startDate = dateRange.startDate() != null ? dateRange.startDate().format(DATE_FORMATTER) : null;
        String endDate = dateRange.endDate() != null ? dateRange.endDate().format(DATE_FORMATTER) : null;
        return new PeriodResponse(startDate, endDate);
    }

    public LocationResponse toLocationResponse(Location location) {
        if (location == null) return null;
        return new LocationResponse(location.addressName(), location.region1depthName(), location.region2depthName(), location.region3depthName(), location.longitude(), location.latitude());
    }

    // BrandStory, Sns, Rating, SearchTags to DTO mappers
    public SnsResponse toSnsResponse(Sns sns) {
        if (sns == null) return null;
        return new SnsResponse(sns.icon(), sns.url());
    }

    public BrandStoryResponse toBrandStoryResponse(BrandStory brandStory) {
        if (brandStory == null) return null;
        List<SnsResponse> snsResponses = brandStory.sns() != null ?
                brandStory.sns().stream().map(this::toSnsResponse).collect(Collectors.toList()) :
                Collections.emptyList();
        return new BrandStoryResponse(brandStory.imageUrls(), snsResponses);
    }

    public SearchTagsResponse toSearchTagsResponse(Popup popup) {
        if (popup == null) {
            return new SearchTagsResponse(null, Collections.emptyList());
        }

        String type = popup.getType() != null ? popup.getType().name() : null;

        List<String> categoryNames = popup.getPopupCategories() != null ?
                popup.getPopupCategories().stream().map(PopupCategory::name).collect(Collectors.toList()) :
                Collections.emptyList();

        return new SearchTagsResponse(type, categoryNames);
    }

    public PopupMapQuery toPopupMapQuery(PopupMapRequest request) {
        com.example.demo.domain.model.popup.PopupType type = StringUtils.hasText(request.type()) ? com.example.demo.domain.model.popup.PopupType.valueOf(request.type()) : null;
        List<String> categories = StringUtils.hasText(request.category()) ?
                Arrays.asList(request.category().split(",")) : null;
        DateRange dateRange = (request.startDate() != null && request.endDate() != null) ?
                new DateRange(request.startDate(), request.endDate()) : null;

        return new PopupMapQuery(
                request.minLatitude(),
                request.maxLatitude(),
                request.minLongitude(),
                request.maxLongitude(),
                type,
                categories,
                dateRange
        );
    }

    public List<PopupMapResponse> toPopupMapResponses(List<Popup> popups) {
        if (popups == null) {
            return Collections.emptyList();
        }
        return popups.stream()
                .map(this::toPopupMapResponse)
                .collect(Collectors.toList());
    }

    private PopupMapResponse toPopupMapResponse(Popup popup) {
        return new PopupMapResponse(
                popup.getId(),
                BigDecimal.valueOf(popup.getLocation().latitude()),
                BigDecimal.valueOf(popup.getLocation().longitude())
        );
    }
} 