package com.example.demo.presentation.controller;

import com.example.demo.application.dto.member.MeResponse;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.common.util.CookieUtils;
import com.example.demo.application.service.MemberService;
import com.example.demo.presentation.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "회원 관리", description = "회원 인증 및 정보 관리 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    public ApiResponse<MeResponse> getMe(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new BusinessException(ErrorType.AUTHENTICATION_REQUIRED);
        }
        MeResponse response = memberService.getMe(principal.getId());
        return new ApiResponse<>("사용자 정보 조회 성공", response);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "현재 사용자를 로그아웃 처리합니다.")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        ResponseCookie responseCookie = CookieUtils.deleteAccessTokenCookie();
        response.setHeader("Set-Cookie", responseCookie.toString());

        return ResponseEntity.ok(new ApiResponse<>("로그아웃이 완료되었습니다.", null));
    }

    @DeleteMapping("/me")
    @Operation(summary = "회원 탈퇴", description = "현재 사용자의 계정을 영구적으로 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteMe(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new BusinessException(ErrorType.AUTHENTICATION_REQUIRED);
        }
        memberService.deleteMember(principal.getId());
        return ResponseEntity.ok(new ApiResponse<>("회원 탈퇴가 완료되었습니다.", null));
    }
} 