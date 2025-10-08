package com.example.demo.domain.model.ban;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 제재 조회 조건을 담는 쿼리 객체.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public sealed class BanQuery {
    private final Long memberId;
    private final BanType type;
    private final Long popupId;
    private final LocalDateTime lastGlobalBannedAt;
    private final boolean isActiveOnly;

    public static ByMemberIdFromAll byMemberIdFromAll(Long memberId) {
        return new ByMemberIdFromAll(memberId);
    }

    public static ByMemberAndPopup byMemberAndPopup(Long memberId, Long popupId) {
        return new ByMemberAndPopup(memberId, popupId);
    }

    public static BanQuery byBanType(BanType banType) {
        return new ByBanType(banType);
    }

    public static final class ByMemberIdFromAll extends BanQuery {
        private ByMemberIdFromAll(Long memberId, BanType type, Long popupId, LocalDateTime lastGlobalBannedAt, boolean isActiveOnly) {
            super(memberId, type, popupId, lastGlobalBannedAt, isActiveOnly);
        }

        public ByMemberIdFromAll(Long memberId) {
            this(memberId, BanType.GLOBAL, null, null, true);
        }
    }

    public static final class ByMemberAndPopup extends BanQuery {
        private ByMemberAndPopup(Long memberId, BanType type, Long popupId, LocalDateTime lastGlobalBannedAt, boolean isActiveOnly) {
            super(memberId, type, popupId, lastGlobalBannedAt, isActiveOnly);
        }

        public ByMemberAndPopup(Long memberId, Long popupId) {
            this(memberId, BanType.STORE, popupId, null, true);
        }
    }

    public static final class ByBanType extends BanQuery {
        private ByBanType(Long memberId, BanType type, Long popupId, LocalDateTime lastGlobalBannedAt, boolean isActiveOnly) {
            super(memberId, type, popupId, lastGlobalBannedAt, isActiveOnly);
        }

        public ByBanType(BanType banType) {
            this(null, banType, null, null, true);
        }
    }
}