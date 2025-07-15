package com.example.demo.application.mapper;

import com.example.demo.application.dto.popup.BrandStoryResponse;
import com.example.demo.application.dto.popup.DayOfWeekInfoResponse;
import com.example.demo.application.dto.popup.LocationResponse;
import com.example.demo.application.dto.popup.PeriodResponse;
import com.example.demo.application.dto.popup.PopupDetailInfoResponse;
import com.example.demo.application.dto.popup.PopupFilterRequest;
import com.example.demo.application.dto.popup.PopupSummaryResponse;
import com.example.demo.application.dto.popup.SearchTagsResponse;
import com.example.demo.application.dto.popup.SnsResponse;
import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.DateRange;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.popup.OpeningHours;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.popup.PopupCategory;
import com.example.demo.domain.model.popup.PopupQuery;
import com.example.demo.domain.model.popup.Sns;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

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

    public PopupQuery toQuery(PopupFilterRequest request) {
        if (request.popupId() != null) {
            return PopupQuery.directPopupId(request.popupId());
        }

        // 한글 타입명을 영어 Enum 값으로 매핑
        List<String> mappedTypes = null;
        if (request.type() != null) {
            mappedTypes = request.type().stream()
                .map(type -> switch (type) {
                    case "체험형" -> "EXPERIENTIAL";
                    case "전시형" -> "EXHIBITION";
                    case "판매형" -> "SALES";
                    default -> type; // 이미 영어면 그대로
                })
                .toList();
        }

        return PopupQuery.withFilters(
            Optional.ofNullable(request.size()).orElse(10),
            mappedTypes,
            request.category(),
            request.startDate(),
            request.endDate(),
            (request.region1DepthName() == null || "전국".equals(request.region1DepthName())) ? null : request.region1DepthName(),
            request.lastPopupId()
        );
    }
    public PopupSummaryResponse toPopupSummaryResponse(Popup popup) {
        if (popup == null) return null;

        return new PopupSummaryResponse(
            popup.getId(),
            popup.getName(),
            popup.getDisplay().imageUrls().getFirst(),
            toLocationResponse(popup.getLocation()),
            calculateDDay(popup.getSchedule().dateRange().endDate()),
            formatPeriod(popup.getSchedule().dateRange())
        );
    }
    private long calculateDDay(LocalDate endDate) {
        if (endDate == null) return -1;
        return endDate.toEpochDay() - LocalDate.now().toEpochDay();
    }

    private String formatPeriod(DateRange period) {
        if (period == null) return "";
        String start = period.startDate() != null ? period.startDate().format(DATE_FORMATTER) : "";
        String end = period.endDate() != null ? period.endDate().format(DATE_FORMATTER) : "";
        return start + " ~ " + end;
    }
} 