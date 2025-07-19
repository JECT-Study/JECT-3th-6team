package com.example.demo.domain.model.notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotificationStatus {

    private final ReadStatus status;
    private final LocalDateTime readAt;

    public static NotificationStatus of(ReadStatus status, LocalDateTime readAt) {
        return new NotificationStatus(status, readAt);
    }

    public static NotificationStatus unread() {
        return new NotificationStatus(ReadStatus.UNREAD, null);
    }

    public NotificationStatus read() {
        if (this.status == ReadStatus.UNREAD) {
            return new NotificationStatus(ReadStatus.READ, LocalDateTime.now());
        }
        return this;
    }

    public boolean isRead() {
        return this.status == ReadStatus.READ;
    }
} 