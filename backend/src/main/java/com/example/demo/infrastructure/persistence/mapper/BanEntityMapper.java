package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.ban.Ban;
import com.example.demo.domain.model.ban.BanType;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.infrastructure.persistence.entity.BanEntity;

import java.time.Duration;

public class BanEntityMapper {

    public static Ban toDomain(BanEntity banEntity, Member member, Popup popup) {
        BanType type = banEntity.getPopupId() == null ? BanType.GLOBAL : BanType.STORE;
        int durationDays = Math.toIntExact(Duration.between(banEntity.getStartAt(), banEntity.getEndAt()).toDays());

        return Ban.builder()
                .id(banEntity.getId())
                .member(member)
                .type(type)
                .bannedAt(banEntity.getStartAt())
                .durationDays(durationDays)
                .popup(popup)
                .build();
    }

    public static BanEntity toEntity(Ban ban) {
        Long popupId = switch (ban.getType()) {
            case BanType.GLOBAL -> null;
            case BanType.STORE -> ban.getPopup().getId();
            case null -> throw new BusinessException(ErrorType.FEATURE_NOT_IMPLEMENTED);
        };

        return BanEntity.builder()
                .id(ban.getId())
                .memberId(ban.getMember().id())
                .popupId(popupId)
                .startAt(ban.getBannedAt())
                .endAt(ban.getBannedAt().plusDays(ban.getDurationDays()))
                .build();
    }
}
