package com.example.demo.application.service;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.mapper.PopupDtoMapperV2;
import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.Rating;
import com.example.demo.domain.port.BrandStoryPort;
import com.example.demo.domain.port.PopupPortV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PopupDetailServiceV2 {

    private final PopupPortV2 popupPortV2;
    private final BrandStoryPort brandStoryPort;
    // private final RatingPort ratingPort; // 주석 처리
    private final PopupDtoMapperV2 popupDtoMapperV2;

    @Transactional(readOnly = true)
    public PopupDetailResponse getPopupDetail(Long popupId) {
        var popup = popupPortV2.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팝업이 존재하지 않습니다."));

        var brandStory = brandStoryPort.findByPopupId(popupId)
                .orElse(new BrandStory(Collections.emptyList(), Collections.emptyList()));

        // Mock 객체 사용
        var rating = new Rating(4.5, 120);

        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), popup.getSchedule().dateRange().startDate());

        return new PopupDetailResponse(
                popup.getId(),
                popup.getDisplay().imageUrls(),
                (int) dDay,
                popupDtoMapperV2.toRatingResponse(rating),
                popup.getName(),
                popupDtoMapperV2.toSearchTagsResponse(popup),
                popupDtoMapperV2.toLocationResponse(popup.getLocation()),
                popupDtoMapperV2.toPeriodResponse(popup.getSchedule().dateRange()),
                popupDtoMapperV2.toBrandStoryResponse(brandStory),
                popupDtoMapperV2.toPopupDetailInfoResponse(popup)
        );
    }
} 