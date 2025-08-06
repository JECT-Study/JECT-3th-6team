package com.example.demo.presentation.controller;

import com.example.demo.application.dto.member.MeResponse;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.common.util.CookieUtils;
import com.example.demo.application.service.MemberService;
import com.example.demo.presentation.ApiResponse;
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
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ApiResponse<MeResponse> getMe(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new BusinessException(ErrorType.AUTHENTICATION_REQUIRED);
        }
        MeResponse response = memberService.getMe(principal.getId());
        return new ApiResponse<>("사용자 정보 조회 성공", response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        ResponseCookie responseCookie = CookieUtils.deleteAccessTokenCookie();
        response.setHeader("Set-Cookie", responseCookie.toString());

        return ResponseEntity.ok(new ApiResponse<>("로그아웃이 완료되었습니다.", null));
    }

    /**
     * 회원 탈퇴 (물리적 삭제)
     */
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMe(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new BusinessException(ErrorType.AUTHENTICATION_REQUIRED);
        }
        memberService.deleteMember(principal.getId());
        return ResponseEntity.ok(new ApiResponse<>("회원 탈퇴가 완료되었습니다.", null));
    }
} 