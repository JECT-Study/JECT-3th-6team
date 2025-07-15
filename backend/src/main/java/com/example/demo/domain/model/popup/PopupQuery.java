package com.example.demo.domain.model.popup;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 팝업 조회를 위한 조건들을 담는 객체.
 * 페이징, 필터링, 정렬 조건 등을 포함할 수 있다.
 */
public record PopupQuery(
    Long popupId,
    int size,
    List<String> types,
    List<String> categories,
    LocalDate startDate,
    LocalDate endDate,
    String region1DepthName,
    Long lastPopupId
) {

    public static PopupQuery directPopupId(Long popupId) {
        return new PopupQuery(popupId, 1, List.of(), List.of(), null, null, null, null);
    }

    public static PopupQuery withFilters(
        int size,
        List<String> types,
        List<String> categories,
        LocalDate startDate,
        LocalDate endDate,
        String region1DepthName,
        Long lastPopupId
    ) {
        return new PopupQuery(
            null,
            size,
            Optional.ofNullable(types).orElse(List.of()),
            Optional.ofNullable(categories).orElse(List.of()),
            startDate,
            endDate,
            region1DepthName,
            lastPopupId
        );
    }
}