package com.example.demo.presentation.controller;

import com.example.demo.application.dto.member.MeResponse;
import com.example.demo.domain.model.Member;
import com.example.demo.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/me")
    public ApiResponse<MeResponse> getMe(@AuthenticationPrincipal Member member) {
        if (member == null) {
            // 이 경우는 JwtAuthenticationFilter에서 뭔가 잘못되었을 때 발생할 수 있습니다.
            // 혹은 토큰 없이 접근했을 때 (이론상 SecurityConfig에서 차단됨)
            throw new IllegalStateException("인증된 사용자 정보를 가져올 수 없습니다.");
        }
        return new ApiResponse<>("사용자 정보 조회 성공", MeResponse.from(member));
    }
} 