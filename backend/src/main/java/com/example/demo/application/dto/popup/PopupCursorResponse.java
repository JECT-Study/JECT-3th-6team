package com.example.demo.application.dto.popup;

import java.util.List;

public record PopupCursorResponse(
    List<PopupSummaryResponse> content,
    Long lastPopupId,
    Boolean hasNext
) {} 