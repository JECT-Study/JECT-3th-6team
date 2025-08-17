package com.example.demo.application.mapper;

import com.example.demo.application.dto.popup.*;
import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.DateRange;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.popup.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
// TODO : 리팩토링 필요
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

    public Popup toDomain(PopupCreateRequest request) {
        DateRange dateRange = new DateRange(request.startDate(), request.endDate());

        List<OpeningHours> openingHours = request.weeklyOpeningHours().stream()
                .map(it -> new OpeningHours(
                        java.time.DayOfWeek.valueOf(it.dayOfWeek()),
                        java.time.LocalTime.parse(it.openTime()),
                        java.time.LocalTime.parse(it.closeTime())
                ))
                .toList();

        WeeklyOpeningHours weeklyOpeningHours = new WeeklyOpeningHours(openingHours);
        PopupSchedule schedule = new PopupSchedule(dateRange, weeklyOpeningHours);

        Location location = new Location(
                request.location().addressName(),
                request.location().region1DepthName(),
                request.location().region2DepthName(),
                request.location().region3DepthName(),
                request.location().longitude(),
                request.location().latitude()
        );

        var content = new com.example.demo.domain.model.popup.PopupContent(
                request.content().introduction(),
                request.content().notice()
        );
        List<Sns> sns = request.sns().stream()
                .map(it -> new Sns(it.iconUrl(), it.linkUrl()))
                .toList();
        PopupDisplay display = new PopupDisplay(request.mainImageUrls(), request.brandStoryImageUrls(), content, sns);

        List<PopupCategory> categories = request.categoryIds().stream()
                .map(id -> new PopupCategory(id, null)) // name은 저장 시 Category 조회로 채움
                .toList();

        return Popup.builder()
                .id(null)
                .name(request.name())
                .location(location)
                .schedule(schedule)
                .display(display)
                .type(PopupType.valueOf(request.type()))
                .popupCategories(categories)
                .build();
    }

    public PopupCreateResponse toCreateResponse(Popup popup) {
        return new PopupCreateResponse(
                popup.getId(),
                popup.getName(),
                popup.getType() != null ? popup.getType().name() : null,
                popup.getDisplay() != null ? popup.getDisplay().mainImageUrls() : java.util.Collections.emptyList()
        );
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

    public SnsResponse toSnsResponse(Sns sns) {
        if (sns == null) return null;
        return new SnsResponse(sns.icon(), sns.url());
    }

    public BrandStoryResponse toBrandStoryResponse(BrandStory brandStory) {
        if (brandStory == null) return null;
        List<SnsResponse> snsResponses = brandStory.sns() != null
                ? brandStory.sns().stream().map(this::toSnsResponse).collect(Collectors.toList())
                : Collections.emptyList();
        return new BrandStoryResponse(brandStory.imageUrls(), snsResponses);
    }

    public SearchTagsResponse toSearchTagsResponse(Popup popup) {
        if (popup == null) {
            return new SearchTagsResponse(null, Collections.emptyList());
        }

        String type = popup.getType() != null ? popup.getType().getKorean() : null;

        List<String> categoryNames = popup.getPopupCategories() != null
                ? popup.getPopupCategories().stream().map(PopupCategory::name).collect(Collectors.toList())
                : Collections.emptyList();

        return new SearchTagsResponse(type, categoryNames);
    }

    public PopupMapQuery toPopupMapQuery(PopupMapRequest request) {
        List<PopupType> types = StringUtils.hasText(request.type()) ?
                Arrays.stream(request.type().split(","))
                        .map(PopupType::fromKorean)
                        .toList() : null;
        List<String> categories = StringUtils.hasText(request.category()) ? Arrays.asList(request.category().split(","))
                : null;
        DateRange dateRange = (request.startDate() != null && request.endDate() != null)
                ? new DateRange(request.startDate(), request.endDate())
                : null;

        return new PopupMapQuery(
                request.minLatitude(),
                request.maxLatitude(),
                request.minLongitude(),
                request.maxLongitude(),
                types,
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

    public PopupQuery toQuery(PopupFilterRequest request) {
        if (request.popupId() != null) {
            return PopupQuery.directPopupId(request.popupId());
        }

        // 키워드가 있을 때는 다른 검색 조건을 무시하고 페이징 관련 조건만 유지
        if (request.keyword() != null && !request.keyword().trim().isEmpty()) {
            return PopupQuery.withFilters(
                    Optional.ofNullable(request.size()).orElse(10),
                    null, // types 무시
                    null, // categories 무시
                    null, // startDate 무시
                    null, // endDate 무시
                    null, // region1DepthName 무시
                    request.lastPopupId(),
                    request.keyword().trim()
            );
        }

        List<String> mappedTypes = null;
        if (request.type() != null) {
            mappedTypes = request.type().stream()
                    .map(type -> PopupType.fromKorean(type).name())
                    .toList();
        }

        return PopupQuery.withFilters(
                Optional.ofNullable(request.size()).orElse(10),
                mappedTypes,
                request.category(),
                request.startDate(),
                request.endDate(),
                (request.region1DepthName() == null || "전국".equals(request.region1DepthName())) ? null : request.region1DepthName(),
                request.lastPopupId(),
                null
        );
    }

    public PopupSummaryResponse toPopupSummaryResponse(Popup popup) {
        if (popup == null) return null;

        return new PopupSummaryResponse(
                popup.getId(),
                popup.getName(),
                popup.getDisplay().mainImageUrls().getFirst(),
                toLocationResponse(popup.getLocation()),
                calculateDDay(popup.getSchedule().dateRange().endDate()),
                formatPeriod(popup.getSchedule().dateRange()),
                toSearchTagsResponse(popup)
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