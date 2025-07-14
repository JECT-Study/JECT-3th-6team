package com.example.demo.domain.model;

/**
 * 회원 도메인 모델.
 * 인증된 사용자의 정보를 나타낸다.
 */
public record Member(
    Long id,
    String name,
    String email
) {} 