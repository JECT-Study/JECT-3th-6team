package com.example.demo.application.service;

import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.WaitingPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 입장 시간 초과 알림을 위한 스케줄러 서비스.
 * 30초마다 입장 가능 시간이 5분 지난 대기자를 찾아서 알림을 발송한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledEnterTimeOverNotificationService {

    private final WaitingPort waitingPort;
    private final WaitingNotificationService waitingNotificationService;

    /**
     * 30초마다 입장 시간 초과 대상을 찾아서 알림 발송
     */
    @Scheduled(fixedDelay = 30_000)
    @Transactional
    public void sendEnterTimeOverNotifications() {
        log.info("입장 시간 초과 알림 스케줄러 시작: {}", LocalDateTime.now());

        // 1. 입장 시간이 5분 지난 대기자들 조회
        List<Waiting> enterTimeOverTargets = findEnterTimeOverTargets();

        if (enterTimeOverTargets.isEmpty()) {
            log.info("입장 시간 초과 알림 대상자가 없습니다.");
            return;
        }

        log.info("입장 시간 초과 알림 대상: {}명", enterTimeOverTargets.size());

        // 2. 각 대상자에게 알림 발송
        enterTimeOverTargets.forEach(waiting -> {
            try {
                sendEnterTimeOverNotification(waiting);
            } catch (Exception e) {
                log.error("입장 시간 초과 알림 발송 실패 - 대기 ID: {}", waiting.id(), e);
                // 알림 발송 실패는 트랜잭션을 롤백하지 않음
            }
        });

        log.info("입장 시간 초과 알림 스케줄러 완료: {}", LocalDateTime.now());
    }

    /**
     * 입장 시간이 5분 ~ 5분 30초 지난 대기자들을 조회한다.
     * 스케줄러가 30초마다 실행되므로, 이 범위에 있는 대기자는 한 번만 조회된다.
     */
    private List<Waiting> findEnterTimeOverTargets() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fiveMinutesAgo = now.minusMinutes(5);
        LocalDateTime fiveMinutesThirtySecondsAgo = now.minusMinutes(5).minusSeconds(30);

        // 모든 WAITING 상태 대기자 조회 후 필터링
        WaitingQuery query = WaitingQuery.forStatus(WaitingStatus.WAITING);
        List<Waiting> allWaiting = waitingPort.findByQuery(query);

        return allWaiting.stream()
                .filter(waiting -> waiting.canEnterAt() != null)
                .filter(waiting -> waiting.canEnterAt().isBefore(fiveMinutesAgo))
                .filter(waiting -> waiting.canEnterAt().isAfter(fiveMinutesThirtySecondsAgo))
                .toList();
    }

    /**
     * 입장 시간 초과 알림을 발송한다.
     */
    private void sendEnterTimeOverNotification(Waiting waiting) {
        log.info("입장 시간 초과 알림 발송 시작 - 대기 ID: {}, 회원 ID: {}, 팝업 ID: {}",
                waiting.id(), waiting.member().id(), waiting.popup().getId());

        waitingNotificationService.sendEnterTimeOverNotification(waiting);

        log.info("입장 시간 초과 알림 발송 완료 - 대기 ID: {}", waiting.id());
    }
}
