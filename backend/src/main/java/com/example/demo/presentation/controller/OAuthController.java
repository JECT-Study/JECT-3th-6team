package com.example.demo.presentation.controller;

import com.example.demo.application.service.OAuth2Service;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.Member;
import com.example.demo.presentation.controller.handler.OAuth2FailureHandler;
import com.example.demo.presentation.controller.handler.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/oauth/kakao")
@RequiredArgsConstructor
@Tag(name = "OAuth 인증", description = "카카오 OAuth 2.0 인증 관련 API")
public class OAuthController {

    private final OAuth2Service oAuth2Service;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Value("${custom.app.frontend-url}")
    private String frontendUrl;

    @GetMapping("/callback")
    @Operation(summary = "카카오 OAuth 콜백", description = "카카오 OAuth 2.0 인증 후 콜백을 처리합니다.")
    public void kakaoCallback(
        @Parameter(description = "인증 코드") @RequestParam(required = false) String code,
        @Parameter(description = "에러 메시지") @RequestParam(required = false) String error,
        @Parameter(description = "상태 값") @RequestParam String state,
        HttpServletResponse response) throws IOException {

        if (error != null) {
            oAuth2FailureHandler.handleFailure(response, new BusinessException(ErrorType.OAUTH_ERROR_RECEIVED, error));
            return;
        }

        try {
            Member member = oAuth2Service.processKakaoLogin(code);
            oAuth2SuccessHandler.onAuthenticationSuccess(response, member, state, frontendUrl);
        } catch (Exception e) {
            oAuth2FailureHandler.handleFailure(response, e);
        }
    }
}
