package com.example.demo.domain.port;

import com.example.demo.domain.model.popup.PopupCategory;
import java.util.List;

/**
 * 팝업 카테고리 도메인의 영속성 처리를 위한 아웃고잉 포트.
 */
public interface PopupCategoryPort {

    /**
     * 모든 팝업 카테고리 목록을 조회한다.
     *
     * @return 전체 팝업 카테고리 목록
     */
    List<PopupCategory> findAll();

    /**
     * 주어진 ID 목록에 해당하는 팝업 카테고리 목록을 조회한다.
     *
     * @param categoryIds 조회할 카테고리의 ID 목록
     * @return 조회된 팝업 카테고리 목록
     */
    List<PopupCategory> findByIds(List<Long> categoryIds);
} 