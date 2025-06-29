package com.example.demo.application.service;

import com.example.demo.application.dto.PopupDetailDto;
import com.example.demo.application.port.out.PopupLoadPort;
import com.example.demo.domain.model.PopupDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 팝업 상세 조회 애플리케이션 서비스.
 */
@Service
@RequiredArgsConstructor
public class PopupDetailReadService {

    private final PopupLoadPort popupLoadPort;

    public PopupDetailDto getPopupDetail(Long popupId) {
        PopupDetail popupDetail = popupLoadPort.findDetailById(popupId)
            .orElseThrow(() -> new IllegalArgumentException("해당 팝업이 존재하지 않습니다."));
        return PopupDetailDto.fromDomain(popupDetail);
    }
}
