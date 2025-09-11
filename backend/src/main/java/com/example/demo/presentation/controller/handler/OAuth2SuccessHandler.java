package com.example.demo.presentation.controller.handler;

import com.example.demo.common.jwt.JwtProperties;
import com.example.demo.common.jwt.JwtTokenProvider;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.common.util.CookieUtils;
import com.example.demo.domain.model.Member;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    public void onAuthenticationSuccess(HttpServletResponse response, Member member, String redirectBase, String path) throws IOException {
        UserPrincipal principal = UserPrincipal.create(member.id(), member.email());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        String accessToken = jwtTokenProvider.createToken(authentication);

        ResponseCookie responseCookie = CookieUtils.createAccessTokenCookie(accessToken, jwtProperties.expirationSeconds());
        response.setHeader("Set-Cookie", responseCookie.toString());

        // path 정규화 및 보안 검증
        String safePath = normalizePath(path);
        
        String redirectUrl = UriComponentsBuilder.fromUriString(redirectBase)
                .replacePath(safePath) // 기존 path 완전히 대체
                .build(true)           // 인코딩 보장
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
    
    /**
     * 경로를 정규화하고 보안 검증을 수행합니다.
     * @param path 정규화할 경로
     * @return 안전한 경로
     */
    private String normalizePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "/";
        }
        
        String safePath = path.trim();
        
        // 스킴/호스트 포함 금지 (방어적 차단)
        if (safePath.startsWith("http://") || safePath.startsWith("https://")) {
            return "/";
        }
        
        // 슬래시로 시작하지 않으면 추가
        if (!safePath.startsWith("/")) {
            safePath = "/" + safePath;
        }
        
        return safePath;
    }
} 