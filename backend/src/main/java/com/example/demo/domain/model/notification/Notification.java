package com.example.demo.domain.model.notification;

import com.example.demo.domain.model.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Notification {

    private final Long id;
    private final Member member;
    private final DomainEvent<?> event;
    private final String content;
    private NotificationStatus status;
    private final LocalDateTime createdAt;

    @Builder
    public Notification(Long id, Member member, DomainEvent<?> event, String content, NotificationStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.event = event;
        this.content = content;
        this.status = (status == null) ? NotificationStatus.unread() : status;
        this.createdAt = (createdAt == null) ? LocalDateTime.now() : createdAt;
    }

    public void read() {
        this.status = this.status.read();
    }

    public boolean isRead() {
        return this.status.isRead();
    }
} 