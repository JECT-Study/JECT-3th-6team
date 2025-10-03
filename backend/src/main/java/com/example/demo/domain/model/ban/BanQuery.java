package com.example.demo.domain.model.ban;

import java.time.LocalDateTime;

/**
 * 제재 조회 조건을 담는 쿼리 객체.
 */
public record BanQuery(
        Long memberId,
        BanType type,
        Long popupId,
        LocalDateTime lastGlobalBannedAt, // 마지막으로 조회된 글로벌 제재 시간
        boolean isActiveOnly // 활성화된 제재만 조회할지 여부
) {

    public static BanQuery byMemberIdFromAll(Long memberId) {
        return new BanQuery(memberId, BanType.GLOBAL, null, null, true);
    }

    public static BanQuery byMemberAndPopup(Long memberId, Long popupId) {
        return new BanQuery(memberId, BanType.STORE, popupId, null, true);
    }

    public static BanQuery byBanType(BanType banType) {
        return new BanQuery(null, banType, null, null, true);
    }
}