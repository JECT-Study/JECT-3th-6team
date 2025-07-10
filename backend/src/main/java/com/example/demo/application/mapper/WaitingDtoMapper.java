package com.example.demo.application.mapper;

import com.example.demo.application.dto.RatingResponse;
import com.example.demo.application.dto.WaitingCreateResponse;
import com.example.demo.application.dto.WaitingResponse;
import com.example.demo.domain.model.DateRange;
import com.example.demo.domain.model.Waiting;
import com.example.demo.domain.model.popup.Popup;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Waiting 도메인 모델과 DTO 간의 변환을 담당하는 Mapper.
 */
@Component
public class WaitingDtoMapper {

    /**
     * Waiting 도메인 모델을 WaitingCreateResponse DTO로 변환
     */
    public WaitingCreateResponse toCreateResponse(Waiting waiting) {
        return new WaitingCreateResponse(
                waiting.id(),
                waiting.popup().getName(),
                waiting.waitingPersonName(),
                waiting.peopleCount(),
                waiting.contactEmail(),
                waiting.waitingNumber(),
                waiting.registeredAt()
        );
    }

    /**
     * Waiting 도메인 모델을 WaitingResponse DTO로 변환
     */
    public WaitingResponse toResponse(Waiting waiting) {
        Popup popup = waiting.popup();
        return new WaitingResponse(
                waiting.id(),
                popup.getId(),
                popup.getName(),
                popup.getDisplay().imageUrls().isEmpty() ? null : popup.getDisplay().imageUrls().getFirst(),
                popup.getLocation().region1depthName() + ", " + popup.getLocation().region2depthName(),
                new RatingResponse(
                        1.0,
                        1
                ),
                formatPeriod(popup.getSchedule().dateRange()),
                waiting.waitingNumber(),
                waiting.status().name()
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