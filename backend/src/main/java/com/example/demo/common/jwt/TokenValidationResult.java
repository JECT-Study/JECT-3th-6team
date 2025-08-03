package com.example.demo.common.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenValidationResult {
    VALID,
    INVALID,
    MALFORMED,
    EXPIRED
}