package com.example.demo.presentation.controller;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.dto.popup.PopupCursorResponse;
import com.example.demo.application.dto.popup.PopupFilterRequest;
import com.example.demo.application.dto.popup.PopupMapRequest;
import com.example.demo.application.dto.popup.PopupMapResponse;
import com.example.demo.application.service.PopupService;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.presentation.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/popups")
@RequiredArgsConstructor
public class PopupController {

    private final PopupService popupService;

    @GetMapping("/map")
    public ApiResponse<List<PopupMapResponse>> getPopupsOnMap(PopupMapRequest request) {
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
}
/**
 * GET http://localhost:8080/api/popups
 * <p>
 * HTTP/1.1 200
 * Vary: Origin
 * Vary: Access-Control-Request-Method
 * Vary: Access-Control-Request-Headers
 * X-Content-Type-Options: nosniff
 * X-XSS-Protection: 0
 * Cache-Control: no-cache, no-store, max-age=0, must-revalidate
 * Pragma: no-cache
 * Expires: 0
 * X-Frame-Options: SAMEORIGIN
 * Content-Type: application/json
 * Transfer-Encoding: chunked
 * Date: Sat, 02 Aug 2025 07:56:01 GMT
 * <p>
 * {
 * "message": "팝업 목록 조회에 성공했습니다.",
 * "data": {
 * "content": [
 * {
 * "popupId": 1,
 * "popupName": "무신사 X 나이키 팝업스토어",
 * "popupImageUrl": "https://example.com/images/popup1.jpg",
 * "location": {
 * "addressName": "경기도 성남시 분당구",
 * "region1depthName": "경기도",
 * "region2depthName": "성남시 분당구",
 * "region3depthName": "",
 * "longitude": 127.423084873712,
 * "latitude": 37.0789561558879
 * },
 * "dDay": -38,
 * "period": "2025.06.01 ~ 2025.06.25",
 * "searchTags": {
 * "type": "체험형",
 * "category": [
 * "패션",
 * "예술"
 * ]
 * }
 * }
 * ],
 * "lastPopupId": 1,
 * "hasNext": false
 * }
 * }
 * 응답 파일이 저장되었습니다.
 * > 2025-08-02T165601.200.json
 * <p>
 * Response code: 200; Time: 105ms (105 ms); Content length: 455 bytes (455 B)
 * <p>
 * 쿠키는 요청 간에 유지됩니다.
 * > /Users/imsubin/IdeaProjects/JECT-3th-6team/.idea/httpRequests/http-client.cookies
 */