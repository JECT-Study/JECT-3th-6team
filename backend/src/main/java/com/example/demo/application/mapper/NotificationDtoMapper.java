package com.example.demo.application.mapper;

import com.example.demo.application.dto.notification.NotificationListResponse;
import com.example.demo.application.dto.notification.NotificationResponse;
import com.example.demo.application.dto.notification.RelatedResourceResponse;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.CursorResult;
import com.example.demo.domain.model.notification.DomainEvent;
import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.waiting.Waiting;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationDtoMapper {

    // 대기 도메인 이벤트 타입별 리소스 변환 전략
    private static final Map<String, Function<Waiting, List<RelatedResourceResponse>>> WAITING_EVENT_STRATEGIES = Map.of(
            "REVIEW_REQUEST", NotificationDtoMapper::createReviewRequestResources,
            "WAITING_CONFIRMED", NotificationDtoMapper::createWaitingConfirmedResources,
            "ENTER_3TEAMS_BEFORE", NotificationDtoMapper::createEnterNotificationResources,
            "ENTER_NOW", NotificationDtoMapper::createEnterNotificationResources,
            "ENTER_TIME_OVER", NotificationDtoMapper::createEnterNotificationResources
    );

    public NotificationListResponse toCursorResponse(CursorResult<Notification> cursorResult) {
        List<NotificationResponse> content = cursorResult.content().stream()
                .map(this::toNotificationResponse)
                .collect(Collectors.toList());

        Long lastNotificationId = content.isEmpty() ? null : content.getLast().notificationId();

        return new NotificationListResponse(
                content,
                lastNotificationId,
                cursorResult.hasNext()
        );
    }

    public NotificationResponse toNotificationResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getEvent().getEventType(),
                notification.getContent(),
                notification.getCreatedAt(),
                notification.isRead(),
                toRelatedResourceResponses(notification.getEvent())
        );
    }

    private List<RelatedResourceResponse> toRelatedResourceResponses(DomainEvent<?> event) {
        Object source = event.getSource();
        String eventType = event.getEventType();

        return switch (source) { // Java 17의 Pattern Matching Switch 문법
            case Waiting waiting -> processWaitingEvent(waiting, eventType);
            case null -> Collections.emptyList();
            default ->
                    throw new BusinessException(ErrorType.UNSUPPORTED_NOTIFICATION_TYPE, source.getClass().getSimpleName());
        };
    }

    private List<RelatedResourceResponse> processWaitingEvent(Waiting waiting, String eventType) {
        return WAITING_EVENT_STRATEGIES.getOrDefault(eventType, w -> List.of())
                .apply(waiting);
    }

    // 리뷰 요청 알림: Popup 정보가 필요 (스토어명, 주소 등)
    private static List<RelatedResourceResponse> createReviewRequestResources(Waiting waiting) {
        List<RelatedResourceResponse> resources = new ArrayList<>();

        // Popup 리소스 추가
        Popup popup = waiting.popup();
        if (popup != null) {
            resources.add(new RelatedResourceResponse("POPUP", Map.of(
                    "id", popup.getId(),
                    "storeName", popup.getName(),
                    "address", popup.getLocation().addressName()
            )));
        }

        // Waiting 리소스 추가 (대기 번호 등)
        resources.add(new RelatedResourceResponse("WAITING", Map.of(
                "id", waiting.id(),
                "waitingNumber", waiting.waitingNumber()
        )));

        return resources;
    }

    // 대기 확정 알림: Waiting과 Popup 정보 모두 필요
    private static List<RelatedResourceResponse> createWaitingConfirmedResources(Waiting waiting) {
        List<RelatedResourceResponse> resources = new ArrayList<>();

        // Popup 리소스 추가 (스토어명)
        Popup popup = waiting.popup();
        if (popup != null) {
            resources.add(new RelatedResourceResponse("POPUP", Map.of(
                    "id", popup.getId(),
                    "storeName", popup.getName()
            )));
        }

        // Waiting 리소스 추가 (대기 번호, 등록 시간 등)
        resources.add(new RelatedResourceResponse("WAITING", Map.of(
                "id", waiting.id(),
                "waitingNumber", waiting.waitingNumber(),
                "registeredAt", waiting.registeredAt()
        )));

        return resources;
    }

    // 입장 관련 알림: Waiting과 Popup 정보 필요
    private static List<RelatedResourceResponse> createEnterNotificationResources(Waiting waiting) {
        List<RelatedResourceResponse> resources = new ArrayList<>();

        // Popup 리소스 추가
        Popup popup = waiting.popup();
        if (popup != null) {
            resources.add(new RelatedResourceResponse("POPUP", Map.of(
                    "id", popup.getId(),
                    "storeName", popup.getName(),
                    "address", popup.getLocation().addressName()
            )));
        }

        // Waiting 리소스 추가
        resources.add(new RelatedResourceResponse("WAITING", Map.of(
                "id", waiting.id(),
                "waitingNumber", waiting.waitingNumber()
        )));

        return resources;
    }
}
