package com.example.demo.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
    KAKAO("kakao");

    private final String registrationId;
} 