package com.example.demo.domain.port;

import com.example.demo.domain.model.notification.Notification;

/**
 * 알림 이벤트 발송을 위한 아웃고잉 포트.
 * 실시간 알림 전송 기능을 추상화한다.
 */
public interface NotificationEventPort {

    /**
     * 특정 회원에게 실시간 알림을 발송한다.
     *
     * @param memberId     알림을 받을 회원 ID
     * @param notification 발송할 알림 도메인 모델
     */
    void sendRealTimeNotification(Long memberId, Notification notification);

    /**
     * 클라이언트 연결을 등록하고 SSE Emitter를 반환한다.
     *
     * @param memberId 연결할 회원 ID
     * @return SSE Emitter 객체
     */
    Object createSseConnection(Long memberId);

    /**
     * 클라이언트 연결을 해제한다.
     *
     * @param memberId     연결 해제할 회원 ID
     * @param connectionId 연결 식별자
     */
    void unregisterConnection(Long memberId, String connectionId);

    /**
     * 특정 회원의 연결 상태를 확인한다.
     *
     * @param memberId 확인할 회원 ID
     * @return 연결되어 있으면 true, 아니면 false
     */
    boolean isConnected(Long memberId);
} 