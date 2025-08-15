package com.example.demo.presentation.controller;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.dto.popup.PopupCursorResponse;
import com.example.demo.application.dto.popup.PopupFilterRequest;
import com.example.demo.application.dto.popup.PopupMapRequest;
import com.example.demo.application.dto.popup.PopupMapResponse;
import com.example.demo.application.dto.popup.PopupCreateRequest;
import com.example.demo.application.dto.popup.PopupCreateResponse;
import com.example.demo.application.service.PopupService;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.presentation.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/popups")
@RequiredArgsConstructor
public class PopupController {

    private final PopupService popupService;
    //TODO 임시 코드 삭제 필요
    @Value("${custom.admin.password}")
    private String adminPassword;

    @GetMapping("/map")
    public ApiResponse<List<PopupMapResponse>> getPopupsOnMap(@Valid PopupMapRequest request) {
        List<PopupMapResponse> response = popupService.getPopupsOnMap(request);
        return new ApiResponse<>("지도 내 팝업 조회가 성공했습니다.", response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PopupCursorResponse>> getPopups(@Valid PopupFilterRequest request) {
        PopupCursorResponse response = popupService.getFilteredPopups(request);
        return ResponseEntity.ok(new ApiResponse<>("팝업 목록 조회에 성공했습니다.", response));
    }

    @GetMapping("/{popupId}")
    public ResponseEntity<ApiResponse<PopupDetailResponse>> getPopupDetail(@PathVariable Long popupId, @AuthenticationPrincipal UserPrincipal principal) {
        Long memberId = Optional.ofNullable(principal).map(UserPrincipal::getId).orElse(null);
        PopupDetailResponse popupDetail = popupService.getPopupDetail(popupId, memberId);
        return ResponseEntity.ok(new ApiResponse<>("팝업 상세 조회가 성공했습니다.", popupDetail));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PopupCreateResponse>> createPopup(
            @Valid @RequestBody PopupCreateRequest request,
            @RequestHeader("Authorization") String password
    ) {
        //TODO 임시 코드 삭제 필요
        if (!password.equals(adminPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("비밀번호가 잘못되었습니다.", null));
        }
        PopupCreateResponse response = popupService.create(request);
        return ResponseEntity.ok(new ApiResponse<>("팝업이 성공적으로 등록되었습니다.", response));
    }
}
