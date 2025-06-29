package com.example.demo.application.port.out;

import com.example.demo.domain.model.Popup;
import com.example.demo.domain.model.PopupDetail;
import java.util.Optional;

/**
 * 팝업 조회용 포트.
 */
public interface PopupLoadPort {

    /**
     * ID로 팝업 엔티티를 로드한다.
     * 존재하지 않을 경우 Optional.empty()를 반환할 수 있다.
     *
     * @param popupId 팝업 ID
     * @return 팝업 도메인 객체
     */
    Optional<Popup> findById(Long popupId);
    /**
     * ID로 팝업 상세 정보를 조회한다.
     * 존재하지 않을 경우 Optional.empty()를 반환한다.
     *
     * @param popupId 조회할 팝업의 고유 식별자
     * @return 팝업 상세 도메인 모델 (없으면 Optional.empty())
     */
    Optional<PopupDetail> findDetailById(Long popupId);

} 