package com.example.demo.domain.model.notification;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class DomainEvent<T> {
    private final T source;
    private final LocalDateTime occurredAt;

    protected DomainEvent(T source) {
        this.source = source;
        this.occurredAt = LocalDateTime.now();
    }

    public abstract String getEventType();
} 