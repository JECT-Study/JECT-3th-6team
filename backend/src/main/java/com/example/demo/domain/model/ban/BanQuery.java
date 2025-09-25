package com.example.demo.domain.model.ban;

/**
 * 제재 조회 조건을 담는 쿼리 객체.
 */
public record BanQuery(
        Long memberId,
        BanType type,
        Long popupId
) {

    public static BanQuery byMemberIdFromAll(Long memberId) {
        return new BanQuery(memberId, BanType.GLOBAL, null);
    }

    public static BanQuery byMemberAndPopup(Long memberId, Long popupId) {
        return new BanQuery(memberId, BanType.STORE, popupId);
    }
}