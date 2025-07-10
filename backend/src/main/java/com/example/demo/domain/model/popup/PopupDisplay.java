package com.example.demo.domain.model.popup;

import java.util.List;

/**
 * 팝업의 전시/표현 정보를 나타내는 값 객체.
 *
 * @param imageUrls 팝업 대표 이미지 URL 목록
 * @param content   소개 및 공지사항
 * @param sns       관련 SNS 목록
 */
public record PopupDisplay(
    List<String> imageUrls,
    PopupContent content,
    List<Sns> sns
) {
} 