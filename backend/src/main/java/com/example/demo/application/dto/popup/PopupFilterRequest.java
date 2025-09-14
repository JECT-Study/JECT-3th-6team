package com.example.demo.application.dto.popup;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;


public record PopupFilterRequest(
    Long popupId,
    Integer size,
    List<String> type,
    List<String> category,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate,
    String region1DepthName,
    Long lastPopupId,
    String keyword
) {}
