package com.example.demo.domain.model.popup;

/**
 * SNS 정보 도메인 모델.
 * 아이콘 URL과 링크 URL을 나타낸다.
 */
public record Sns(
    String icon,
    String url
) {} 