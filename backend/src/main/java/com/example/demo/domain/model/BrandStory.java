package com.example.demo.domain.model;

import com.example.demo.domain.model.popup.Sns;
import java.util.List;

/**
 * 브랜드 스토리 도메인 모델.
 * 이미지 URL 리스트와 SNS 리스트를 포함한다.
 */
public record BrandStory(
    List<String> imageUrls,
    List<Sns> sns
) {
}
