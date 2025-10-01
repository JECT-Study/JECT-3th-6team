package com.example.demo.application.service;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.dto.popup.*;
import com.example.demo.application.dto.popup.PopupCursorResponse.PopupListElementResponse;
import com.example.demo.application.mapper.PopupDtoMapper;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.popup.PopupMapQuery;
import com.example.demo.domain.model.popup.PopupQuery;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.BrandStoryPort;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.domain.port.WaitingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupPort popupPort;
    private final BrandStoryPort brandStoryPort;
    private final PopupDtoMapper popupDtoMapper;
    private final WaitingPort waitingPort;

    @Transactional(readOnly = true)
    public List<PopupMapResponse> getPopupsOnMap(PopupMapRequest request) {
        PopupMapQuery query = popupDtoMapper.toPopupMapQuery(request);
        List<Popup> popups = popupPort.findByMapQuery(query);
        return popupDtoMapper.toPopupMapResponses(popups);
    }

    @Transactional(readOnly = true)
    public PopupCursorResponse getFilteredPopups(PopupFilterRequest request) {
        // TODO: https://github.com/JECT-Study/JECT-3th-6team/pull/92#discussion_r2210591165
        PopupQuery query = popupDtoMapper.toQuery(request);
        int size = Optional.ofNullable(request.size()).orElse(10);
        List<Popup> popups = popupPort.findByQuery(query);
        boolean hasNext = popups.size() > size;
        List<PopupListElementResponse> content = popups.stream()
                .limit(size)
                .map(popup -> {
                    int waitingCount = waitingPort.findByQuery(WaitingQuery.forPopup(popup.getId(), WaitingStatus.WAITING)).size();
                    return popupDtoMapper.toPopupListElementResponse(popup, waitingCount);
                })
                .toList();
        Long lastPopupId = content.isEmpty() ? null : content.getLast().popupId();
        return new PopupCursorResponse(content, lastPopupId, hasNext);
    }

    @Transactional(readOnly = true)
    public PopupDetailResponse getPopupDetail(Long popupId, Long memberId) {
        var popup = popupPort.findById(popupId)
                .orElseThrow(() -> new BusinessException(ErrorType.POPUP_NOT_FOUND, String.valueOf(popupId)));

        var brandStory = brandStoryPort.findByPopupId(popupId)
                .orElse(new BrandStory(Collections.emptyList(), Collections.emptyList()));
        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), popup.getSchedule().dateRange().endDate());
        WaitingStatus status = calculateReservationStatus(popupId, memberId);

        return new PopupDetailResponse(
                popup.getId(),
                popup.getDisplay().mainImageUrls(),
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

    @Transactional
    public PopupCreateResponse create(PopupCreateRequest request) {
        Popup domain = popupDtoMapper.toDomain(request);
        Popup saved = popupPort.save(domain);
        return popupDtoMapper.toCreateResponse(saved);
    }
} 