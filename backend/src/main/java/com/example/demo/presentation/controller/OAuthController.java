package com.example.demo.presentation.controller;

import com.example.demo.application.service.OAuth2Service;
import com.example.demo.common.jwt.JwtProperties;
import com.example.demo.common.jwt.JwtTokenProvider;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.domain.model.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Controller
@RequestMapping("/oauth/kakao")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuth2Service oAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Value("${custom.app.frontend-url}")
    private String frontendUrl;
    
    @GetMapping("/callback")
    public void kakaoCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws IOException {
        Member member = oAuth2Service.processKakaoLogin(code);
        
        UserPrincipal principal = UserPrincipal.create(member.id());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        
        String accessToken = jwtTokenProvider.createToken(authentication);
        
        response.addCookie(createAccessTokenCookie(accessToken));
        
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                .path(state)
                .build()
                .toUriString();
        response.sendRedirect(redirectUrl);
    }
    
    private Cookie createAccessTokenCookie(String accessToken) {
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS 환경에서만 전송
        cookie.setPath("/");
        cookie.setMaxAge(jwtProperties.expirationSeconds().intValue());
        return cookie;
    }
} 