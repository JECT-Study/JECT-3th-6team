package com.example.demo.domain.port;

import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.popup.PopupMapQuery;
import com.example.demo.domain.model.popup.PopupQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * 팝업 도메인의 영속성 처리를 위한 아웃고잉 포트 (V2).
 * 인프라스트럭처 계층은 이 포트의 구현을 책임진다.
 */
public interface PopupPort {

    /**
     * 팝업 정보를 저장하거나 수정한다.
     * ID가 없으면 새로 생성하고, 있으면 수정한다.
     *
     * @param popup 저장할 팝업 도메인 모델
     * @return 영속화된 팝업 도메인 모델 (ID가 포함됨)
     */
    Popup save(Popup popup);

    /**
     * 팝업 ID로 팝업을 조회한다.
     *
     * @param popupId 조회할 팝업의 ID
     * @return 조회된 팝업 정보를 담은 Optional 객체
     */
    Optional<Popup> findById(Long popupId);

    /**
     * 특정 조건에 맞는 팝업 목록을 조회한다.
     * 페이징, 필터링, 정렬 조건을 포함할 수 있다.
     *
     * @param query 조회 조건을 담은 객체
     * @return 조건에 맞는 팝업 목록
     */
    List<Popup> findByQuery(PopupQuery query);

    /**
     * 팝업 ID로 팝업을 삭제한다.
     *
     * @param popupId 삭제할 팝업의 ID
     */
    void deleteById(Long popupId);
    
    /**
     * 지도 영역 내 팝업 목록을 조회한다.
     *
     * @param query 조회 조건을 담은 객체
     * @return 조건에 맞는 팝업 목록
     */
    List<Popup> findByMapQuery(PopupMapQuery query);
} 