package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.notification.ScheduledNotification;
import com.example.demo.domain.model.notification.ScheduledNotificationTrigger;
import com.example.demo.infrastructure.persistence.entity.NotificationEntity;
import com.example.demo.infrastructure.persistence.entity.ScheduledNotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * ScheduledNotification 도메인 모델과 ScheduledNotificationEntity 간의 변환을 담당하는 Mapper.
 */
@Component
@RequiredArgsConstructor
public class ScheduledNotificationEntityMapper {

    private final NotificationEntityMapper notificationEntityMapper;

    public ScheduledNotificationEntity toEntity(ScheduledNotification scheduledNotification) {
        Notification reservatedNotification = scheduledNotification.getReservatedNotification();
        NotificationEntity notificationEntity = notificationEntityMapper.toEntity(reservatedNotification);

        ScheduledNotificationTrigger notificationTrigger = scheduledNotification.getScheduledNotificationTrigger();

        return ScheduledNotificationEntity.builder()
                .id(notificationEntity.getId())
                .memberId(notificationEntity.getMemberId())
                .sourceDomain(notificationEntity.getSourceDomain())
                .sourceId(notificationEntity.getSourceId())
                .eventType(notificationEntity.getEventType())
                .content(notificationEntity.getContent())
                .status(notificationEntity.getStatus())
                .readAt(notificationEntity.getReadAt())
                .trigger(notificationTrigger)
                .build();

    }
} 