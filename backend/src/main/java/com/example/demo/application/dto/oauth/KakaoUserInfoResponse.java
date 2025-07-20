package com.example.demo.application.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Optional;

public record KakaoUserInfoResponse(
    Long id,
    @JsonProperty("connected_at") String connectedAt,
    Map<String, Object> properties,
    @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
    public record KakaoAccount(
        @JsonProperty("profile_nickname_needs_agreement") Boolean profileNicknameNeedsAgreement,
        Profile profile,
        @JsonProperty("email_needs_agreement") Boolean emailNeedsAgreement,
        @JsonProperty("is_email_valid") Boolean isEmailValid,
        @JsonProperty("is_email_verified") Boolean isEmailVerified,
        String email
    ) {}

    public record Profile(
        String nickname
    ) {}
    
    public String getNickname() {
        return Optional.ofNullable(properties.get("nickname"))
            .map(Object::toString)
            .orElse("unknown");
    }
    
    public String getEmail() {
        return this.kakaoAccount.email();
    }
} 