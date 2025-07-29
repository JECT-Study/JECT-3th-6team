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

    public void onAuthenticationSuccess(HttpServletResponse response, Member member, String state, String frontendUrl) throws IOException {
        UserPrincipal principal = UserPrincipal.create(member.id(), member.email());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        String accessToken = jwtTokenProvider.createToken(authentication);

        ResponseCookie responseCookie = CookieUtils.createAccessTokenCookie(accessToken, jwtProperties.expirationSeconds());
        response.setHeader("Set-Cookie", responseCookie.toString());

        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                .path(state)
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
} 