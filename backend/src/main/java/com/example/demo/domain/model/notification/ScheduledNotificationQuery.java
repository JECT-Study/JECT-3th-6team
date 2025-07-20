package com.example.demo.domain.model.notification;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 스케줄된 알림 조회를 위한 쿼리 객체.
 */
@Getter
@Builder
public class ScheduledNotificationQuery {
    
    private final Boolean processed;                    // 처리 여부 (null: 전체, true: 처리됨, false: 미처리)
    private final ScheduledNotificationTrigger trigger; // 특정 트리거 타입 (null: 전체)
    private final LocalDateTime createdAfter;           // 생성 시간 이후
    private final LocalDateTime createdBefore;          // 생성 시간 이전
    private final Integer limit;                        // 조회 개수 제한
    
    /**
     * 미처리 스케줄된 알림들 조회용 쿼리 생성
     */
    public static ScheduledNotificationQuery forPendingNotifications() {
        return ScheduledNotificationQuery.builder()
                .processed(false)
                .limit(100) // 한 번에 최대 100개만 처리
                .build();
    }
    
    /**
     * 특정 트리거 타입의 미처리 알림들 조회용 쿼리 생성
     */
    public static ScheduledNotificationQuery forPendingNotificationsByTrigger(ScheduledNotificationTrigger trigger) {
        return ScheduledNotificationQuery.builder()
                .processed(false)
                .trigger(trigger)
                .limit(50)
                .build();
    }
    
    /**
     * 오래된 처리 완료 알림들 정리용 쿼리 생성
     */
    public static ScheduledNotificationQuery forOldProcessedNotifications(LocalDateTime cutoffTime) {
        return ScheduledNotificationQuery.builder()
                .processed(true)
                .createdBefore(cutoffTime)
                .limit(500)
                .build();
    }
}
