package com.example.demo.application.mapper;

import com.example.demo.application.dto.notification.NotificationListResponse;
import com.example.demo.application.dto.notification.NotificationResponse;
import com.example.demo.application.dto.notification.RelatedResourceResponse;
import com.example.demo.domain.model.CursorResult;
import com.example.demo.domain.model.notification.DomainEvent;
import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.waiting.Waiting;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationDtoMapper {

    // 대기 도메인 이벤트 타입별 리소스 변환 전략
    private static final Map<String, Function<Waiting, RelatedResourceResponse>> WAITING_EVENT_STRATEGIES = Map.of(
            "REVIEW_REQUEST", waiting -> new RelatedResourceResponse("POPUP", waiting.popup().getId()),
            "WAITING_CONFIRMED", waiting -> new RelatedResourceResponse("WAITING", waiting.id()),
            "ENTER_3TEAMS_BEFORE", waiting -> new RelatedResourceResponse("WAITING", waiting.id()),
            "ENTER_NOW", waiting -> new RelatedResourceResponse("WAITING", waiting.id()),
            "ENTER_TIME_OVER", waiting -> new RelatedResourceResponse("WAITING", waiting.id())
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
                toRelatedResourceResponse(notification.getEvent())
        );
    }

    private RelatedResourceResponse toRelatedResourceResponse(DomainEvent<?> event) {
        Object source = event.getSource();
        String eventType = event.getEventType();

        return switch (source) { // Java 17의 Pattern Matching Switch 문법
            case Waiting waiting -> processWaitingEvent(waiting, eventType);
            default -> throw new IllegalArgumentException(
                    "지원하지 않는 도메인 이벤트 소스입니다: " + source.getClass().getSimpleName()
            );
        };
    }

    private RelatedResourceResponse processWaitingEvent(Waiting waiting, String eventType) {
        return Optional.ofNullable(WAITING_EVENT_STRATEGIES.get(eventType))
                .map(strategy -> strategy.apply(waiting))
                .orElseThrow(() -> new IllegalArgumentException(
                        "지원하지 않는 대기 이벤트 타입입니다: " + eventType
                ));
    }
}
