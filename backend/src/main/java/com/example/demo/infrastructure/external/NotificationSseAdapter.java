package com.example.demo.infrastructure.external;

import com.example.demo.application.dto.notification.NotificationResponse;
import com.example.demo.application.mapper.NotificationDtoMapper;
import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.port.NotificationEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE를 통한 실시간 알림 발송 구현체.
 * NotificationEventPort의 구현체로 SSE 기술을 사용한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSseAdapter implements NotificationEventPort {

    private final NotificationDtoMapper notificationDtoMapper;

    // 회원별 SSE 연결 관리
    private final Map<Long, Map<String, SseEmitter>> memberConnections = new ConcurrentHashMap<>();

    private static final long SSE_TIMEOUT = 30 * 60 * 1000L; // 30분

    @Override
    public void sendRealTimeNotification(Long memberId, Notification notification) {
        Optional.ofNullable(memberConnections.get(memberId))
                .filter(connections -> !connections.isEmpty())
                .ifPresentOrElse(
                        connections -> sendNotificationToConnections(memberId, notification, connections),
                        () -> log.debug("회원 ID {}에 대한 SSE 연결이 없습니다. 실시간 알림을 스킵합니다.", memberId)
                );
    }

    private void sendNotificationToConnections(Long memberId, Notification notification, Map<String, SseEmitter> connections) {
        NotificationResponse response = notificationDtoMapper.toNotificationResponse(notification);

        // 연결된 모든 SSE Emitter에 알림 전송 실패시 연결 제거
        connections.entrySet().removeIf(entry ->
                !sendNotificationToSingleConnection(memberId, notification.getId(), entry, response)
        );

        // 모든 연결이 제거된 경우 해당 회원의 연결 정보도 삭제
        cleanupEmptyConnectionsIfNeeded(memberId, connections);
    }

    private boolean sendNotificationToSingleConnection(Long memberId, Long notificationId, Map.Entry<String, SseEmitter> entry, NotificationResponse response) {
        String connectionId = entry.getKey();
        SseEmitter emitter = entry.getValue();

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("notification")
                            .data(response)
            );

            log.debug("회원 ID {}의 연결 {}로 실시간 알림을 전송했습니다. 알림 ID: {}", memberId, connectionId, notificationId);
            return true;
        } catch (IOException e) {
            log.warn("회원 ID {}의 연결 {}로 알림 전송 실패: {}", memberId, connectionId, e.getMessage());
            emitter.completeWithError(e);
            return false;
        }
    }

    private void cleanupEmptyConnectionsIfNeeded(Long memberId, Map<String, SseEmitter> connections) {
        if (connections.isEmpty()) {
            memberConnections.remove(memberId);
        }
    }

    @Override
    public Object createSseConnection(Long memberId) {
        String connectionId = UUID.randomUUID().toString();

        SseEmitter emitter = createSseEmitter(memberId, connectionId);
        memberConnections.computeIfAbsent(memberId, k -> new ConcurrentHashMap<>())
                .put(connectionId, emitter);

        log.info("회원 ID {}의 SSE 연결이 등록되었습니다. 연결 ID: {}", memberId, connectionId);
        return emitter;
    }

    @Override
    public void unregisterConnection(Long memberId, String connectionId) {
        Map<String, SseEmitter> connections = memberConnections.get(memberId);
        if (connections == null) {
            return;
        }

        SseEmitter emitter = connections.remove(connectionId);
        if (emitter != null) {
            emitter.complete();
            log.info("회원 ID {}의 SSE 연결이 해제되었습니다. 연결 ID: {}", memberId, connectionId);
        } else {
            log.debug("연결 ID {}가 존재하지 않습니다.", connectionId);
        }

        cleanupEmptyConnectionsIfNeeded(memberId, connections);
    }

    @Override
    public boolean isConnected(Long memberId) {
        return Optional.ofNullable(memberConnections.get(memberId))
                .map(connections -> !connections.isEmpty())
                .orElse(false);
    }

    /**
     * SSE Emitter를 생성하고 이벤트 핸들러를 설정한다.
     */
    private SseEmitter createSseEmitter(Long memberId, String connectionId) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);

        setupEmitterEventHandlers(emitter, memberId, connectionId);
        sendConnectionSuccessEvent(emitter, memberId, connectionId);

        return emitter;
    }

    private void setupEmitterEventHandlers(SseEmitter emitter, Long memberId, String connectionId) {
        // 연결 완료 시 처리
        emitter.onCompletion(() -> {
            log.debug("회원 ID {}의 SSE 연결이 완료되었습니다. 연결 ID: {}", memberId, connectionId);
            removeConnection(memberId, connectionId);
        });

        // 타임아웃 시 처리
        emitter.onTimeout(() -> {
            log.debug("회원 ID {}의 SSE 연결이 타임아웃되었습니다. 연결 ID: {}", memberId, connectionId);
            removeConnection(memberId, connectionId);
        });

        // 에러 시 처리
        emitter.onError(throwable -> {
            log.warn("회원 ID {}의 SSE 연결에서 에러가 발생했습니다. 연결 ID: {}, 에러: {}",
                    memberId, connectionId, throwable.getMessage());
            removeConnection(memberId, connectionId);
        });
    }

    private void sendConnectionSuccessEvent(SseEmitter emitter, Long memberId, String connectionId) {
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("SSE 연결이 성공했습니다."));
        } catch (IOException e) {
            log.error("회원 ID {}의 연결 성공 이벤트 전송 실패: {}", memberId, e.getMessage());
            removeConnection(memberId, connectionId);
        }
    }

    /**
     * 연결을 안전하게 제거한다.
     */
    private void removeConnection(Long memberId, String connectionId) {
        Optional.ofNullable(memberConnections.get(memberId))
                .ifPresent(connections -> {
                    connections.remove(connectionId);
                    cleanupEmptyConnectionsIfNeeded(memberId, connections);
                });
    }

    /**
     * 현재 연결 상태를 반환한다. (모니터링용)
     */
    public Map<Long, Integer> getConnectionStatus() {
        Map<Long, Integer> status = new ConcurrentHashMap<>();
        memberConnections.forEach((memberId, connections) -> 
            status.put(memberId, connections.size()));
        return status;
    }

    @Override
    public void sendHeartbeatToAllConnections() {
        log.debug("전체 {} 명의 회원에게 하트비트 ping 전송", memberConnections.size());
        
        memberConnections.entrySet().removeIf(memberEntry -> {
            Long memberId = memberEntry.getKey();
            Map<String, SseEmitter> connections = memberEntry.getValue();
            
            // 해당 회원의 모든 연결에 ping 전송
            connections.entrySet().removeIf(connectionEntry -> 
                !sendPingToConnection(memberId, connectionEntry)
            );
            
            // 연결이 모두 제거된 회원은 맵에서 제거
            boolean shouldRemoveMember = connections.isEmpty();
            if (shouldRemoveMember) {
                log.debug("회원 ID {}의 모든 연결이 제거되어 회원 정보를 삭제합니다.", memberId);
            }
            
            return shouldRemoveMember;
        });
    }

    private boolean sendPingToConnection(Long memberId, Map.Entry<String, SseEmitter> connectionEntry) {
        String connectionId = connectionEntry.getKey();
        SseEmitter emitter = connectionEntry.getValue();
        
        try {
            emitter.send(SseEmitter.event()
                    .name("ping")
                    .data(Map.of(
                        "timestamp", System.currentTimeMillis(),
                        "message", "heartbeat",
                        "connectionId", connectionId
                    )));
            
            log.trace("회원 ID {}의 연결 {}로 ping 전송 성공", memberId, connectionId);
            return true; // 연결 유지
            
        } catch (IOException e) {
            log.warn("회원 ID {}의 연결 {}로 ping 전송 실패: {}", memberId, connectionId, e.getMessage());
            emitter.completeWithError(e);
            return false; // 연결 제거
        }
    }
} 