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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "팝업 관리", description = "팝업스토어 관련 API")
public class PopupController {

    private final PopupService popupService;
    @Value("${custom.admin.password}")
    private String adminPassword;

    @GetMapping("/map")
    @Operation(summary = "지도 내 팝업 조회", description = "지정된 지도 영역 내의 팝업스토어 목록을 조회합니다.")
    public ApiResponse<List<PopupMapResponse>> getPopupsOnMap(
            @Parameter(description = "지도 영역 정보") PopupMapRequest request) {
        List<PopupMapResponse> response = popupService.getPopupsOnMap(request);
        return new ApiResponse<>("지도 내 팝업 조회가 성공했습니다.", response);
    }

    @GetMapping
    @Operation(summary = "팝업 목록 조회", description = "필터 조건에 따른 팝업스토어 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<PopupCursorResponse>> getPopups(
            @Parameter(description = "필터 조건") PopupFilterRequest request) {
        PopupCursorResponse response = popupService.getFilteredPopups(request);
        return ResponseEntity.ok(new ApiResponse<>("팝업 목록 조회에 성공했습니다.", response));
    }

    @GetMapping("/{popupId}")
    @Operation(summary = "팝업 상세 조회", description = "특정 팝업스토어의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<PopupDetailResponse>> getPopupDetail(
            @Parameter(description = "팝업 ID") @PathVariable Long popupId, 
            @AuthenticationPrincipal UserPrincipal principal) {
        Long memberId = Optional.ofNullable(principal).map(UserPrincipal::getId).orElse(null);
        PopupDetailResponse popupDetail = popupService.getPopupDetail(popupId, memberId);
        return ResponseEntity.ok(new ApiResponse<>("팝업 상세 조회가 성공했습니다.", popupDetail));
    }

    @PostMapping
    @Operation(summary = "팝업 생성", description = "새로운 팝업스토어를 등록합니다.")
    public ResponseEntity<ApiResponse<PopupCreateResponse>> createPopup(
            @Parameter(description = "팝업 생성 정보") @RequestBody PopupCreateRequest request,
            @Parameter(description = "관리자 비밀번호") @RequestHeader("Authorization") String password
    ) {
        if (!password.equals(adminPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("비밀번호가 잘못되었습니다.", null));
        }
        PopupCreateResponse response = popupService.create(request);
        return ResponseEntity.ok(new ApiResponse<>("팝업이 성공적으로 등록되었습니다.", response));
    }
}
