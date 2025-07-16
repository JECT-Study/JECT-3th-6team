package com.example.demo.application.service;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.dto.popup.PopupFilterRequest;
import com.example.demo.application.dto.popup.PopupSummaryResponse;
import com.example.demo.application.dto.popup.PopupCursorResponse;
import com.example.demo.application.mapper.PopupDtoMapper;
import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.popup.PopupQuery;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.BrandStoryPort;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.domain.port.WaitingPort;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupPort popupPort;
    private final BrandStoryPort brandStoryPort;
    private final PopupDtoMapper popupDtoMapper;
    private final WaitingPort waitingPort;

    @Transactional(readOnly = true)
    public PopupCursorResponse getFilteredPopups(PopupFilterRequest request) {
        PopupQuery query = popupDtoMapper.toQuery(request);
        int size = Optional.ofNullable(request.size()).orElse(10);
        List<Popup> popups = popupPort.findByQuery(query);
        boolean hasNext = popups.size() > size;
        List<PopupSummaryResponse> content = popups.stream()
            .limit(size)
            .map(popupDtoMapper::toPopupSummaryResponse)
            .toList();
        Long lastPopupId = content.isEmpty() ? null : content.getLast().popupId();
        return new PopupCursorResponse(content, lastPopupId, hasNext);
    }

    @Transactional(readOnly = true)
    public PopupDetailResponse getPopupDetail(Long popupId, Long memberId) {
        var popup = popupPort.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팝업이 존재하지 않습니다."));

        var brandStory = brandStoryPort.findByPopupId(popupId)
                .orElse(new BrandStory(Collections.emptyList(), Collections.emptyList()));
        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), popup.getSchedule().dateRange().startDate());
        WaitingStatus status = calculateReservationStatus(popupId, memberId);

        return new PopupDetailResponse(
                popup.getId(),
                popup.getDisplay().imageUrls(),
                (int) dDay,
                popup.getName(),
                popupDtoMapper.toSearchTagsResponse(popup),
                popupDtoMapper.toLocationResponse(popup.getLocation()),
                popupDtoMapper.toPeriodResponse(popup.getSchedule().dateRange()),
                popupDtoMapper.toBrandStoryResponse(brandStory),
                popupDtoMapper.toPopupDetailInfoResponse(popup),
                status
        );
    }
    private WaitingStatus calculateReservationStatus(Long popupId, Long memberId) {
        if (memberId == null) return WaitingStatus.NONE;

        return waitingPort.findByMemberIdAndPopupId(memberId, popupId)
            .map(Waiting::status)
            .orElse(WaitingStatus.NONE);
    }

} 