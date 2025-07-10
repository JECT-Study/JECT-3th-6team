package com.example.demo.domain.model.popup;

/**
 * 팝업의 소개 및 공지사항 등 텍스트성 콘텐츠를 나타내는 값 객체.
 *
 * @param introduction 소개
 * @param notice       공지사항
 */
public record PopupContent(
    String introduction,
    String notice
) {
} 