package com.example.demo.domain.model.popup;

/**
 * 팝업의 카테고리를 나타내는 도메인 모델.
 *
 * @param id   카테고리 ID
 * @param name 카테고리 이름 (예: 패션, 뷰티)
 */
public record PopupCategory(
    Long id,
    String name
) {
} 