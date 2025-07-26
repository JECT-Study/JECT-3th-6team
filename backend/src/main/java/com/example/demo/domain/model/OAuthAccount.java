package com.example.demo.domain.model;

public record OAuthAccount(
    Long id,
    OAuthProvider provider,
    String providerId,
    Long memberId
) {} 