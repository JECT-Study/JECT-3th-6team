package com.example.demo.application.mapper;

import com.example.demo.application.dto.popup.PopupSummaryResponse;
import com.example.demo.application.dto.waiting.WaitingCreateResponse;
import com.example.demo.application.dto.waiting.WaitingResponse;
import com.example.demo.domain.model.DateRange;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.waiting.Waiting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Waiting 도메인 모델과 DTO 간의 변환을 담당하는 Mapper.
 */
@Component
@RequiredArgsConstructor
public class WaitingDtoMapper {

    private final PopupDtoMapper popupDtoMapper;

    /**
     * Waiting 도메인 모델을 WaitingCreateResponse DTO로 변환
     */
    public WaitingCreateResponse toCreateResponse(Waiting waiting) {
        Popup popup = waiting.popup();
        return new WaitingCreateResponse(
                waiting.id(),
                popup.getName(),
                waiting.waitingPersonName(),
                waiting.peopleCount(),
                waiting.contactEmail(),
                waiting.waitingNumber(),
                waiting.registeredAt(),
                popupDtoMapper.toLocationResponse(popup.getLocation()),
                popup.getDisplay().mainImageUrls().getFirst()
        );
    }

    /**
     * Waiting 도메인 모델을 WaitingResponse DTO로 변환 (팀 수 포함)
     */
    public WaitingResponse toResponse(Waiting waiting, Integer waitingCount) {
        Popup popup = waiting.popup();
        LocalDate popupEndDate = popup.getSchedule().dateRange().endDate();
        LocalDate now = LocalDate.now();
        long dDay = ChronoUnit.DAYS.between(now, popupEndDate);

        // 예상 대기 시간이 null이면 0으로 처리 (입장 데이터가 없는 경우)
        Integer expectedWaitingTime = waiting.expectedWaitingTimeMinutes() != null 
                ? waiting.expectedWaitingTimeMinutes() 
                : 0;

        return new WaitingResponse(
                waiting.id(),
                waiting.waitingNumber(),
                waiting.status().name(),
                waiting.waitingPersonName(),
                waiting.peopleCount(),
                waiting.contactEmail(),
                new PopupSummaryResponse(
                        popup.getId(),
                        popup.getName(),
                        popup.getDisplay().mainImageUrls().isEmpty() ? null : popup.getDisplay().mainImageUrls().getFirst(),
                        popupDtoMapper.toLocationResponse(popup.getLocation()),
                        dDay,
                        formatPeriod(popup.getSchedule().dateRange()),
                        popupDtoMapper.toSearchTagsResponse(popup)
                ),
                waiting.registeredAt(),
                expectedWaitingTime,
                waitingCount
        );
    }

    /**
     * 기간을 클라이언트 표시용 포맷으로 변환
     */
    private String formatPeriod(DateRange dateRange) {
        LocalDate startDate = dateRange.startDate();
        LocalDate endDate = dateRange.endDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "%s ~ %s".formatted(formatter.format(startDate), formatter.format(endDate));
    }
} 
