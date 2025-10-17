package com.example.demo.presentation.controller;

import com.example.demo.application.dto.image.ImageUploadResponse;
import com.example.demo.application.dto.popup.PopupCreateRequest;
import com.example.demo.application.dto.popup.PopupCreateResponse;
import com.example.demo.application.service.AdminService;
import com.example.demo.presentation.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Admin", description = "관리자 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 관리자 로그인 (세션 토큰 발급)
     */
    @Operation(summary = "관리자 로그인", description = "관리자 비밀번호를 확인하고 세션 토큰을 발급합니다")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(
            @RequestBody Map<String, String> request
    ) {
        String password = request.get("password");
        String token = adminService.loginAndCreateSession(password);
        return ResponseEntity.ok(new ApiResponse<>("로그인 성공", Map.of("token", token)));
    }

    /**
     * 관리자 로그아웃 (세션 삭제)
     */
    @Operation(summary = "관리자 로그아웃", description = "세션을 종료합니다")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader("X-Admin-Token") String token
    ) {
        adminService.logout(token);
        return ResponseEntity.ok(new ApiResponse<>("로그아웃 성공", null));
    }

    /**
     * 팝업 목록 조회
     */
    @Operation(summary = "팝업 목록 조회", description = "모든 팝업 목록을 조회합니다")
    @GetMapping("/popups")
    public ResponseEntity<ApiResponse<List<AdminService.AdminPopupSummary>>> getPopups(
            @RequestHeader("X-Admin-Token") String token
    ) {
        adminService.verifySession(token);
        List<AdminService.AdminPopupSummary> popups = adminService.getAllPopups();
        return ResponseEntity.ok(new ApiResponse<>("팝업 목록 조회 성공", popups));
    }

    /**
     * 팝업 삭제
     */
    @Operation(summary = "팝업 삭제", description = "특정 팝업을 삭제합니다")
    @DeleteMapping("/popups/{popupId}")
    public ResponseEntity<ApiResponse<Void>> deletePopup(
            @RequestHeader("X-Admin-Token") String token,
            @PathVariable Long popupId
    ) {
        adminService.verifySession(token);
        adminService.deletePopup(popupId);
        return ResponseEntity.ok(new ApiResponse<>("팝업 삭제 성공", null));
    }

    /**
     * 특정 팝업의 대기 목록 조회
     */
    @Operation(summary = "대기 목록 조회", description = "특정 팝업의 대기 목록을 조회합니다")
    @GetMapping("/popups/{popupId}/waitings")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getWaitings(
            @RequestHeader("X-Admin-Token") String token,
            @PathVariable Long popupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        adminService.verifySession(token);
        
        List<AdminService.AdminWaitingSummary> waitings = adminService.getWaitingsByPopup(popupId, page, size);
        int totalCount = adminService.getTotalWaitingCount(popupId);
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        return ResponseEntity.ok(new ApiResponse<>("대기 목록 조회 성공", Map.of(
                "waitings", waitings,
                "page", page,
                "size", size,
                "totalCount", totalCount,
                "totalPages", totalPages
        )));
    }

    /**
     * 대기 입장 처리
     */
    @Operation(summary = "대기 입장", description = "대기 중인 고객을 입장 처리합니다 (0번만 가능)")
    @PostMapping("/waitings/{waitingId}/enter")
    public ResponseEntity<ApiResponse<Void>> enterWaiting(
            @RequestHeader("X-Admin-Token") String token,
            @PathVariable Long waitingId
    ) {
        adminService.verifySession(token);
        adminService.enterWaiting(waitingId);
        return ResponseEntity.ok(new ApiResponse<>("입장 처리 성공", null));
    }

    /**
     * 팝업 생성 (관리자 전용)
     */
    @Operation(summary = "팝업 생성", description = "새로운 팝업스토어를 등록합니다 (관리자 전용)")
    @PostMapping("/popups/create")
    public ResponseEntity<ApiResponse<PopupCreateResponse>> createPopup(
            @RequestHeader("X-Admin-Token") String token,
            @RequestBody @Valid PopupCreateRequest request
    ) {
        adminService.verifySession(token);
        PopupCreateResponse response = adminService.createPopup(request);
        return ResponseEntity.ok(new ApiResponse<>("팝업이 성공적으로 등록되었습니다", response));
    }

    /**
     * 이미지 업로드 (관리자 전용)
     */
    @Operation(summary = "이미지 업로드", description = "이미지 파일을 업로드합니다 (관리자 전용)")
    @PostMapping("/images/upload")
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadImage(
            @RequestHeader("X-Admin-Token") String token,
            @RequestParam("file") MultipartFile file
    ) {
        adminService.verifySession(token);
        ImageUploadResponse response = adminService.uploadImage(file);
        return ResponseEntity.ok(new ApiResponse<>("이미지 업로드 성공", response));
    }
}

