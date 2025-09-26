package com.example.demo.domain.model.ban;

import java.time.LocalDate;

/**
 * 글로벌 밴 도메인 모델.
 * 사용자의 누적 노쇼로 인한 전체 팝업 예약 제한을 나타낸다.
 */
public record GlobalBan(
        Long id,
        Long memberId,
        LocalDate startDate,
        LocalDate endDate
) {
    /**
     * 글로벌 밴이 현재 유효한지 확인한다.
     * 
     * @return 밴이 유효한 경우 true
     */
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    /**
     * 밴 기간이 만료되었는지 확인한다.
     * 
     * @return 밴이 만료된 경우 true
     */
    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }

    /**
     * 밴이 시작되었는지 확인한다.
     * 
     * @return 밴이 시작된 경우 true
     */
    public boolean isStarted() {
        return !LocalDate.now().isBefore(startDate);
    }
}
