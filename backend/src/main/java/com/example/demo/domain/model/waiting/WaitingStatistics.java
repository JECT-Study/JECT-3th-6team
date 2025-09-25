package com.example.demo.domain.model.waiting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WaitingStatistics {
    private final Long id;
    private final Long popupId;
    private final Long waitingId;
    private final int initialWaitingNumber;
    private final LocalDateTime reservedAt;
    private final LocalDateTime enteredAt;

    public Duration getTotalWaitingTime() {
        if (enteredAt == null) {
            return null;
        }
        return Duration.between(reservedAt, enteredAt);
    }

    public Double getTimePerPerson() {
        Duration totalTime = getTotalWaitingTime();
        if (totalTime == null || initialWaitingNumber == 0) {
            return null;
        }
        return (double) totalTime.toMinutes() / initialWaitingNumber;
    }
}