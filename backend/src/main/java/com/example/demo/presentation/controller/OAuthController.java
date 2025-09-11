package com.example.demo.presentation.controller;

import com.example.demo.application.service.OAuth2Service;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.common.jwt.JwtTokenProvider;
import com.example.demo.common.util.RedirectUrlValidator;
import com.example.demo.domain.model.Member;
import com.example.demo.presentation.controller.handler.OAuth2FailureHandler;
import com.example.demo.presentation.controller.handler.OAuth2SuccessHandler;
import io.jsonwebtoken.Claims;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final RedirectUrlValidator redirectUrlValidator;

    @Value("${custom.app.frontend-url}")
    private String frontendUrl;

    /**
     * OAuth 로그인을 시작합니다. 안전한 state 토큰을 발급합니다.
     * @param redirect 리다이렉트할 URL (선택사항)
     * @param path 리다이렉트할 경로 (기본값: "/")
     * @param response HTTP 응답
     * @throws IOException 리다이렉트 실패 시
     */
    @GetMapping("/login")
    public void startKakaoLogin(@RequestParam(required = false) String redirect,
                               @RequestParam(defaultValue = "/") String path,
                               HttpServletResponse response) throws IOException {
        
        // redirect URL 검증
        String validatedRedirect = redirectUrlValidator.getValidatedRedirectUrl(redirect);
        
        // 안전한 state 토큰 발급
        String stateToken = jwtTokenProvider.createStateToken(validatedRedirect, path);
        
        // Spring Security OAuth2가 처리할 수 있도록 state를 쿼리 파라미터로 전달
        String oauthUrl = "/oauth2/authorization/kakao?state=" + stateToken;
        
        log.info("Starting OAuth login with state token, redirect: {}, path: {}", validatedRedirect, path);
        response.sendRedirect(oauthUrl);
    }

    @GetMapping("/callback")
    @Operation(summary = "카카오 OAuth 콜백", description = "카카오 OAuth 2.0 인증 후 콜백을 처리합니다.")
    public void kakaoCallback(
        @Parameter(description = "인증 코드") @RequestParam(required = false) String code,
        @Parameter(description = "에러 메시지") @RequestParam(required = false) String error,
        @Parameter(description = "상태 값") @RequestParam String state,
        HttpServletResponse response) throws IOException {

        if (error != null) {
            log.warn("OAuth error received: {}", error);
            oAuth2FailureHandler.handleFailure(response, new BusinessException(ErrorType.OAUTH_ERROR_RECEIVED, error));
            return;
        }

        try {
            // state 토큰 검증
            Claims claims = jwtTokenProvider.verifyStateToken(state);
            String redirectUrl = (String) claims.get("redirect");
            String path = (String) claims.getOrDefault("path", "/");
            
            // redirect URL 재검증 (이중 안전장치)
            String validatedRedirectUrl = redirectUrlValidator.getValidatedRedirectUrl(redirectUrl);
            
            // 로그인 처리
            Member member = oAuth2Service.processKakaoLogin(code);
            
            // 성공 핸들러 호출
            oAuth2SuccessHandler.onAuthenticationSuccess(response, member, validatedRedirectUrl, path);
            
        } catch (IllegalArgumentException e) {
            log.warn("State token validation failed: {}", e.getMessage());
            oAuth2FailureHandler.handleFailure(response, new BusinessException(ErrorType.OAUTH_ERROR_RECEIVED, "Invalid state token"));
        } catch (Exception e) {
            log.error("OAuth callback processing failed", e);
            oAuth2FailureHandler.handleFailure(response, e);
        }
    }
}
