package com.example.demo.domain.port;

import com.example.demo.domain.model.waiting.PopupWaitingStatistics;

/**
 * 대기 통계 정보에 대한 포트 인터페이스.
 */
public interface WaitingStatisticsPort {

    /**
     * 특정 팝업의 입장 완료된 대기 통계를 조회한다.
     *
     * @param popupId 팝업 ID
     * @return 팝업의 대기 통계 컬렉션
     */
    PopupWaitingStatistics findCompletedStatisticsByPopupId(Long popupId);
}