package com.example.demo.presentation.controller;

import com.example.demo.application.dto.PopupDetailDto;
import com.example.demo.application.service.PopupDetailReadService;
import com.example.demo.presentation.dto.PopupDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/popups")
@RequiredArgsConstructor
public class PopupController {

    private final PopupDetailReadService popupDetailReadService;

    @GetMapping("/{popupId}")
    public ResponseEntity<PopupDetailResponse> getPopupDetail(@PathVariable Long popupId) {
        PopupDetailDto popupDetailDto = popupDetailReadService.getPopupDetail(popupId);
        return ResponseEntity.ok(PopupDetailResponse.fromApplicationDto(popupDetailDto));
    }
}
