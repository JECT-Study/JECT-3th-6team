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

    /**
     * 입장 완료된 Waiting 객체로부터 WaitingStatistics를 생성한다.
     *
     * @param waiting 입장 완료된 대기 정보
     * @return 생성된 대기 통계
     */
    public static WaitingStatistics fromCompletedWaiting(Waiting waiting) {
        if (waiting.enteredAt() == null) {
            throw new IllegalArgumentException("입장 완료된 대기 정보만 통계로 변환할 수 있습니다");
        }

        return WaitingStatistics.builder()
                .popupId(waiting.popup().getId())
                .waitingId(waiting.id())
                .initialWaitingNumber(waiting.waitingNumber())
                .reservedAt(waiting.registeredAt())
                .enteredAt(waiting.enteredAt())
                .build();
    }
}