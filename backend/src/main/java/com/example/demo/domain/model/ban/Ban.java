package com.example.demo.domain.model.ban;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.Popup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Ban {
    private final Long id;
    private final Member member;
    private final BanType type;
    private final LocalDateTime bannedAt;
    private final int durationDays;
    private final Popup popup;

    public LocalDateTime getExpiresAt() {
        return bannedAt.plusDays(durationDays);
    }

    public boolean isActive() {
        return LocalDateTime.now().isBefore(getExpiresAt());
    }
}