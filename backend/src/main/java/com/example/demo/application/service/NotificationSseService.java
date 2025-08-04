package com.example.demo.application.service;

import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.port.NotificationEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE를 통한 실시간 알림 서비스.
 * 클라이언트 연결 관리와 실시간 알림 발송을 담당한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSseService {

    private final NotificationEventPort notificationEventPort;

    /**
     * 회원의 SSE 연결을 생성한다.
     *
     * @param memberId 연결할 회원 ID
     * @return SSE Emitter (컨트롤러에서 반환)
     */
    public SseEmitter createSseConnection(Long memberId) {
        log.info("회원 ID {}의 SSE 연결을 생성합니다.", memberId);
        
        Object emitter = notificationEventPort.createSseConnection(memberId);
        
        log.info("회원 ID {}의 SSE 연결이 성공적으로 생성되었습니다.", memberId);
        
        return (SseEmitter) emitter;
    }

    /**
     * 특정 회원에게 실시간 알림을 발송한다.
     *
     * @param memberId     알림을 받을 회원 ID
     * @param notification 발송할 알림
     */
    public void sendRealTimeNotification(Long memberId, Notification notification) {
        log.debug("회원 ID {}에게 실시간 알림을 발송합니다. 알림 ID: {}", memberId, notification.getId());
        
        notificationEventPort.sendRealTimeNotification(memberId, notification);
    }

    /**
     * 특정 회원의 연결 상태를 확인한다.
     *
     * @param memberId 확인할 회원 ID
     * @return 연결되어 있으면 true, 아니면 false
     */
    public boolean isConnected(Long memberId) {
        return notificationEventPort.isConnected(memberId);
    }

    /**
     * 회원의 SSE 연결을 해제한다.
     *
     * @param memberId     연결 해제할 회원 ID
     * @param connectionId 연결 식별자
     */
    public void disconnectSse(Long memberId, String connectionId) {
        log.info("회원 ID {}의 SSE 연결을 해제합니다. 연결 ID: {}", memberId, connectionId);
        
        notificationEventPort.unregisterConnection(memberId, connectionId);
    }
} 