package com.example.demo.presentation.controller;

import com.example.demo.application.service.OAuth2Service;
import com.example.demo.domain.model.Member;
import com.example.demo.presentation.controller.handler.OAuth2FailureHandler;
import com.example.demo.presentation.controller.handler.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth/kakao")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuth2Service oAuth2Service;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Value("${custom.app.frontend-url}")
    private String frontendUrl;

    @GetMapping("/callback")
    public void kakaoCallback(@RequestParam(required = false) String code,
        @RequestParam(required = false) String error,
        @RequestParam String state,
        HttpServletResponse response) throws IOException {

        if (error != null) {
            oAuth2FailureHandler.handleFailure(response, new IllegalStateException("OAuth2 error param received: " + error));
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
