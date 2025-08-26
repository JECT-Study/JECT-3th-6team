package com.example.demo.common.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "custom.jwt.access-token")
public record JwtProperties(
    String secret,
    Long expirationSeconds
) {} 