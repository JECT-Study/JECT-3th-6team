package com.example.demo.domain.model.popup;

import java.util.List;

/**
 * 팝업의 전시/표현 정보를 나타내는 값 객체.
 *
 * @param mainImageUrls 팝업 메인 이미지 URL 목록
 * @param brandStoryImageUrls 브랜드 스토리 이미지 URL 목록
 * @param content   소개 및 공지사항
 * @param sns       관련 SNS 목록
 */
public record PopupDisplay(
    List<String> mainImageUrls,
    List<String> brandStoryImageUrls,
    PopupContent content,
    List<Sns> sns
) {
} 