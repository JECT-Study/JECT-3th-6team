package com.example.demo.application.service;

import com.example.demo.application.dto.notification.NotificationListRequest;
import com.example.demo.application.dto.notification.NotificationListResponse;
import com.example.demo.application.dto.notification.NotificationReadRequest;
import com.example.demo.application.dto.notification.NotificationResponse;
import com.example.demo.application.dto.notification.NotificationDeleteRequest;
import com.example.demo.application.mapper.NotificationDtoMapper;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
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
            throw new BusinessException(ErrorType.INVALID_PAGE_SIZE, "페이지 크기는 0보다 커야 합니다: " + size);
        }
        if (size > MAX_SIZE) {
            throw new BusinessException(ErrorType.INVALID_PAGE_SIZE, "페이지 크기는 " + MAX_SIZE + "을 초과할 수 없습니다: " + size);
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
            throw new BusinessException(ErrorType.INVALID_READ_STATUS, readStatusStr);
        }
    }

    private NotificationSortOrder parseSortOrder(String sortStr) {
        if (sortStr == null) {
            return DEFAULT_SORT_ORDER;
        }

        try {
            return NotificationSortOrder.valueOf(sortStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorType.INVALID_SORT_OPTION, sortStr);
        }
    }

    public NotificationResponse markNotificationAsRead(Long memberId, NotificationReadRequest request) {
        Notification notification = notificationPort.findAllBy(NotificationQuery.findByNotificationId(request.notificationId()))
                .content().stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorType.NOTIFICATION_NOT_FOUND, String.valueOf(request.notificationId())));

        if (!notification.getMember().id().equals(memberId)) {
            throw new BusinessException(ErrorType.ACCESS_DENIED_NOTIFICATION, String.valueOf(request.notificationId()));
        }

        notification.read();
        notificationPort.save(notification);
        return notificationDtoMapper.toNotificationResponse(notification);
    }

    public void deleteNotification(Long memberId, NotificationDeleteRequest request) {
        Notification notification = notificationPort.findAllBy(NotificationQuery.findByNotificationId(request.notificationId()))
                .content().stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorType.NOTIFICATION_NOT_FOUND, String.valueOf(request.notificationId())));

        if (!notification.getMember().id().equals(memberId)) {
            throw new BusinessException(ErrorType.ACCESS_DENIED_NOTIFICATION, String.valueOf(request.notificationId()));
        }

        notificationPort.delete(notification);
    }
} 