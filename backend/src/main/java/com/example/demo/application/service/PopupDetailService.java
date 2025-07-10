package com.example.demo.application.service;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.mapper.PopupDtoMapper;
import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.Rating;
import com.example.demo.domain.port.BrandStoryPort;
import com.example.demo.domain.port.PopupPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PopupDetailService {

    private final PopupPort popupPort;
    private final BrandStoryPort brandStoryPort;
    // private final RatingPort ratingPort; // 주석 처리
    private final PopupDtoMapper popupDtoMapper;

    @Transactional(readOnly = true)
    public PopupDetailResponse getPopupDetail(Long popupId) {
        var popup = popupPort.findById(popupId)
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
                popupDtoMapper.toRatingResponse(rating),
                popup.getName(),
                popupDtoMapper.toSearchTagsResponse(popup),
                popupDtoMapper.toLocationResponse(popup.getLocation()),
                popupDtoMapper.toPeriodResponse(popup.getSchedule().dateRange()),
                popupDtoMapper.toBrandStoryResponse(brandStory),
                popupDtoMapper.toPopupDetailInfoResponse(popup)
        );
    }
} 