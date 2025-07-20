package com.example.demo.application.service;

import com.example.demo.application.dto.oauth.KakaoTokenResponse;
import com.example.demo.application.dto.oauth.KakaoUserInfoResponse;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.OAuthAccount;
import com.example.demo.domain.model.OAuthProvider;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.domain.port.OAuthAccountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final MemberPort memberPort;
    private final OAuthAccountPort oAuthAccountPort;
    private final RestTemplate restTemplate;
    
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    @Transactional
    public Member processKakaoLogin(String authorizationCode) {
        String accessToken = getKakaoAccessToken(authorizationCode);
        KakaoUserInfoResponse userInfo = getKakaoUserInfo(accessToken);

        String providerId = userInfo.id().toString();
        return oAuthAccountPort.findByProviderAndProviderId(OAuthProvider.KAKAO, providerId)
            .map(oauthAccount -> memberPort.findById(oauthAccount.memberId())
                .orElseThrow(() -> new IllegalStateException("OAuth 계정과 연결된 회원을 찾을 수 없습니다.")))
            .orElseGet(() -> {
                String nickname = userInfo.getNickname();
                String email = userInfo.getEmail();

                Member newMember = memberPort.save(new Member(null, nickname, email));
                OAuthAccount newOAuthAccount = new OAuthAccount(null, OAuthProvider.KAKAO, providerId, newMember.id());
                oAuthAccountPort.save(newOAuthAccount);

                return newMember;
            });
    }

    private String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("client_secret", kakaoClientSecret);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        KakaoTokenResponse response = restTemplate.postForObject(kakaoTokenUri, request, KakaoTokenResponse.class);

        if (response == null) {
            throw new IllegalStateException("카카오 토큰 응답을 받지 못했습니다.");
        }
        return response.accessToken();
    }

    private KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<?> request = new HttpEntity<>(headers);

        KakaoUserInfoResponse response = restTemplate.postForObject(kakaoUserInfoUri, request, KakaoUserInfoResponse.class);

        if (response == null) {
            throw new IllegalStateException("카카오 사용자 정보 응답을 받지 못했습니다.");
        }
        return response;
    }
} 