package com.example.demo.domain.port;

import com.example.demo.domain.model.CursorResult;
import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.notification.NotificationQuery;

public interface NotificationPort {

    Notification save(Notification notification);

    CursorResult<Notification> findAllBy(NotificationQuery query);
} 