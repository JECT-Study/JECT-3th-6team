package com.example.demo.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 관리자 세션 토큰 관리 서비스
 */
@Slf4j
@Service
public class AdminSessionService {

    // 메모리 기반 세션 저장소 (프로덕션에서는 Redis 등 사용 권장)
    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();
    
    // 세션 만료 시간 (30분)
    private static final long SESSION_TIMEOUT_MINUTES = 30;

    /**
     * 세션 정보
     */
    private record SessionInfo(LocalDateTime createdAt, LocalDateTime lastAccessedAt) {
        public SessionInfo updateAccessTime() {
            return new SessionInfo(createdAt, LocalDateTime.now());
        }

        public boolean isExpired() {
            return lastAccessedAt.plusMinutes(SESSION_TIMEOUT_MINUTES).isBefore(LocalDateTime.now());
        }
    }

    /**
     * 새로운 세션 토큰 생성
     */
    public String createSession() {
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        sessions.put(token, new SessionInfo(now, now));
        
        log.info("새로운 관리자 세션 생성: token={}", token);
        
        // 만료된 세션 정리
        cleanupExpiredSessions();
        
        return token;
    }

    /**
     * 세션 유효성 검증
     */
    public boolean validateSession(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        SessionInfo session = sessions.get(token);
        
        if (session == null) {
            return false;
        }

        if (session.isExpired()) {
            sessions.remove(token);
            log.info("만료된 세션 제거: token={}", token);
            return false;
        }

        // 마지막 접근 시간 갱신
        sessions.put(token, session.updateAccessTime());
        return true;
    }

    /**
     * 세션 삭제 (로그아웃)
     */
    public void deleteSession(String token) {
        if (token != null) {
            sessions.remove(token);
            log.info("세션 삭제: token={}", token);
        }
    }

    /**
     * 만료된 세션 정리
     */
    private void cleanupExpiredSessions() {
        sessions.entrySet().removeIf(entry -> {
            if (entry.getValue().isExpired()) {
                log.info("만료된 세션 정리: token={}", entry.getKey());
                return true;
            }
            return false;
        });
    }

    /**
     * 전체 세션 수 조회 (모니터링용)
     */
    public int getActiveSessionCount() {
        cleanupExpiredSessions();
        return sessions.size();
    }
}


