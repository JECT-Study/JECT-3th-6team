package com.example.demo.application.dto.popup;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;


public record PopupFilterRequest(
    // TODO: https://github.com/JECT-Study/JECT-3th-6team/pull/92#discussion_r2210604215
    Long popupId,
    @Min(1)
    Integer size,
    @Size(max = 3)
    List<String> type,
    @Size(max = 3)
    List<String> category,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate,
    String region1DepthName,
    Long lastPopupId
) {}
