package com.example.demo.domain.port;

import com.example.demo.domain.model.notification.ScheduledNotification;
import com.example.demo.domain.model.notification.ScheduledNotificationQuery;

import java.util.List;

public interface ScheduledNotificationPort {

    ScheduledNotification save(ScheduledNotification scheduledNotification);

    List<ScheduledNotification> findAllByQuery(ScheduledNotificationQuery query);

    void delete(List<ScheduledNotification> scheduledNotifications);
}
