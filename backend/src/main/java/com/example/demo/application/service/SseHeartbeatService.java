package com.example.demo.application.service;

import com.example.demo.domain.port.NotificationEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * SSE 연결의 하트비트를 관리하는 서비스.
 * 주기적으로 ping을 전송하여 데드 커넥션을 자동으로 정리한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.sse.heartbeat.enabled", havingValue = "true", matchIfMissing = true)
public class SseHeartbeatService {
    
    private final NotificationEventPort notificationEventPort;
    
    @Value("${app.sse.heartbeat.ping-interval:30000}") // 기본 30초
    private long pingInterval;
    
    @Scheduled(fixedRateString = "${app.sse.heartbeat.ping-interval:30000}")
    public void sendHeartbeatPings() {
        log.debug("SSE 하트비트 ping 전송 시작");
        
        try {
            // 모든 연결된 회원들에게 ping 전송
            notificationEventPort.sendHeartbeatToAllConnections();
            
            log.debug("SSE 하트비트 ping 전송 완료");
            
        } catch (Exception e) {
            log.error("SSE 하트비트 ping 전송 중 오류 발생: {}", e.getMessage(), e);
        }
    }
} 