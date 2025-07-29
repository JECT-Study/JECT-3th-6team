package com.example.demo.application.service;

import com.example.demo.application.dto.notification.NotificationListRequest;
import com.example.demo.application.dto.notification.NotificationListResponse;
import com.example.demo.application.dto.notification.NotificationReadRequest;
import com.example.demo.application.dto.notification.NotificationResponse;
import com.example.demo.application.dto.notification.NotificationDeleteRequest;
import com.example.demo.application.mapper.NotificationDtoMapper;
import com.example.demo.domain.model.CursorResult;
import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.notification.NotificationQuery;
import com.example.demo.domain.model.notification.NotificationSortOrder;
import com.example.demo.domain.model.notification.ReadStatus;
import com.example.demo.domain.port.NotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationPort notificationPort;
    private final NotificationDtoMapper notificationDtoMapper;

    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;
    private static final NotificationSortOrder DEFAULT_SORT_ORDER = NotificationSortOrder.TIME_DESC;

    public NotificationListResponse getNotifications(NotificationListRequest request) {
        int pageSize = validateAndSetPageSize(request.size());

        ReadStatus status = parseReadStatus(request.readStatus());

        NotificationSortOrder sortOrder = parseSortOrder(request.sort());

        NotificationQuery query = NotificationQuery.builder()
                .memberId(request.memberId())
                .lastNotificationId(request.lastNotificationId())
                .readStatus(status)
                .pageSize(pageSize)
                .sortOrder(sortOrder)
                .build();

        CursorResult<Notification> result = notificationPort.findAllBy(query);

        return notificationDtoMapper.toCursorResponse(result);
    }

    private int validateAndSetPageSize(Integer size) {
        if (size == null) {
            return DEFAULT_SIZE;
        }
        if (size <= 0) {
            throw new IllegalArgumentException("페이지 크기는 0보다 커야 합니다.");
        }
        if (size > MAX_SIZE) {
            throw new IllegalArgumentException("페이지 크기는 " + MAX_SIZE + "을 초과할 수 없습니다.");
        }
        return size;
    }

    private ReadStatus parseReadStatus(String readStatusStr) {
        if (readStatusStr == null || "ALL".equalsIgnoreCase(readStatusStr)) {
            return null; // null이면 전체 조회
        }

        try {
            return ReadStatus.valueOf(readStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 읽음 상태입니다: " + readStatusStr);
        }
    }

    private NotificationSortOrder parseSortOrder(String sortStr) {
        if (sortStr == null) {
            return DEFAULT_SORT_ORDER;
        }

        try {
            return NotificationSortOrder.valueOf(sortStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 정렬 옵션입니다: " + sortStr);
        }
    }

    public NotificationResponse markNotificationAsRead(Long memberId, NotificationReadRequest request) {
        Notification notification = notificationPort.findAllBy(NotificationQuery.findByNotificationId(request.notificationId()))
                .content().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다: " + request.notificationId()));

        if (!notification.getMember().id().equals(memberId)) {
            throw new IllegalArgumentException("해당 알림은 다른 회원의 것입니다: " + request.notificationId());
        }

        notification.read();
        notificationPort.save(notification);
        return notificationDtoMapper.toNotificationResponse(notification);
    }

    public void deleteNotification(Long memberId, NotificationDeleteRequest request) {
        Notification notification = notificationPort.findAllBy(NotificationQuery.findByNotificationId(request.notificationId()))
                .content().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다: " + request.notificationId()));

        if (!notification.getMember().id().equals(memberId)) {
            throw new IllegalArgumentException("해당 알림은 다른 회원의 것입니다: " + request.notificationId());
        }

        notificationPort.delete(notification);
    }
} 