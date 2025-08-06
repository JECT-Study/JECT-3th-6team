package com.example.demo.domain.port;

import com.example.demo.domain.model.notification.ScheduledNotification;
import com.example.demo.domain.model.notification.ScheduledNotificationQuery;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledNotificationPort {

    ScheduledNotification save(ScheduledNotification scheduledNotification);

    List<ScheduledNotification> findAllByQuery(ScheduledNotificationQuery query);

    void delete(List<ScheduledNotification> scheduledNotifications);

    /**
     * 입장 알림 발송 시간 업데이트
     *
     * @param scheduledNotificationId 스케줄된 알림 ID
     * @param sentAt 발송 시간
     */
    void updateEnterNotificationSentAt(Long scheduledNotificationId, LocalDateTime sentAt);

    /**
     * 실제 입장 시간 업데이트 (웨이팅 ID 기준으로 해당하는 모든 스케줄 업데이트)
     *
     * @param waitingId 웨이팅 ID
     * @param actualEnterTime 실제 입장 시간
     */
    void updateActualEnterTime(Long waitingId, LocalDateTime actualEnterTime);
}
