package com.example.demo.presentation.controller;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.dto.popup.PopupFilterRequest;
import com.example.demo.application.dto.popup.PopupSummaryResponse;
import com.example.demo.application.service.PopupService;
import com.example.demo.presentation.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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

    private final PopupService popupService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PopupSummaryResponse>>> getPopups(@Valid PopupFilterRequest request){
        List<PopupSummaryResponse> popups = popupService.getFilteredPopups(request);
        return ResponseEntity.ok(new ApiResponse<>("팝업 목록 조회에 성공했습니다.", popups));
    }

    @GetMapping("/{popupId}")
    public ResponseEntity<ApiResponse<PopupDetailResponse>> getPopupDetail(@PathVariable Long popupId) {
        // TODO 로그인한 유저의 정보를 넘길 예정
        PopupDetailResponse popupDetail = popupService.getPopupDetail(popupId, 1000L);
        return ResponseEntity.ok(new ApiResponse<>("팝업 상세 조회가 성공했습니다.", popupDetail));
    }
}
