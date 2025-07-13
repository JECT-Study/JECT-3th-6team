package com.example.demo.domain.model.waiting;

import com.example.demo.domain.model.notification.DomainEvent;
import lombok.Getter;

@Getter
public class WaitingDomainEvent extends DomainEvent<Waiting> {

    private final WaitingEventType eventType;

    public WaitingDomainEvent(Waiting source, WaitingEventType eventType) {
        super(source);
        this.eventType = eventType;
    }

    @Override
    public String getEventType() {
        return eventType.name();
    }
} 