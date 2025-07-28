package com.example.demo.domain.port;

import com.example.demo.domain.model.BrandStory;
import java.util.Optional;

/**
 * 브랜드 스토리 도메인의 영속성 처리를 위한 아웃고잉 포트.
 */
public interface BrandStoryPort {

    /**
     * 특정 팝업 ID와 연관된 브랜드 스토리를 조회한다.
     *
     * @param popupId 브랜드 스토리를 조회할 팝업의 ID
     * @return 조회된 브랜드 스토리 정보를 담은 Optional 객체
     */
    Optional<BrandStory> findByPopupId(Long popupId);
} 