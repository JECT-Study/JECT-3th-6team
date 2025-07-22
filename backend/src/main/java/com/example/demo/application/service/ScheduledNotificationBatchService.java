package com.example.demo.application.service;

import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.notification.ScheduledNotification;
import com.example.demo.domain.model.notification.ScheduledNotificationQuery;
import com.example.demo.domain.model.notification.ScheduledNotificationTrigger;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.port.NotificationPort;
import com.example.demo.domain.port.ScheduledNotificationPort;
import com.example.demo.domain.port.WaitingPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 스케줄된 알림을 배치로 처리하는 서비스.
 * 각 트리거 타입별 조건 체크 로직을 포함한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledNotificationBatchService {

    private final ScheduledNotificationPort scheduledNotificationPort;
    private final NotificationPort notificationPort;
    private final WaitingPort waitingPort;

    /**
     * 30초마다 스케줄된 알림들을 체크하여 트리거 조건 만족 시 발송
     */
    @Scheduled(fixedDelay = 30_000)
    @Transactional
    public void processScheduledNotifications() {
        List<ScheduledNotification> pendingNotifications = getPendingScheduledNotifications();

        if (pendingNotifications.isEmpty()) {
            return;
        }

        log.debug("스케줄된 알림 배치 처리 시작 - 대상: {}개", pendingNotifications.size());

        int processedCount = 0;

        List<ScheduledNotification> needToSend = pendingNotifications.stream().filter(this::isTriggerConditionSatisfied).toList();

        for (ScheduledNotification scheduledNotification : needToSend) {
            sendScheduledNotification(scheduledNotification);
            processedCount++;
        }

        if (processedCount > 0) {
            log.info("스케줄된 알림 배치 처리 완료 - 발송: {}개", processedCount);
        }
    }

    /**
     * 트리거 조건 만족 여부 체크 (알림 정책 구현)
     */
    private boolean isTriggerConditionSatisfied(ScheduledNotification scheduledNotification) {
        ScheduledNotificationTrigger trigger = scheduledNotification.getScheduledNotificationTrigger();

        return switch (trigger) {
            case WAITING_ENTER_NOW -> checkEnterNowTrigger(scheduledNotification);
            case WAITING_ENTER_TIME_OVER -> checkEnterTimeOverTrigger(scheduledNotification);
            case WAITING_REVIEW_REQUEST -> checkReviewRequestTrigger(scheduledNotification);
            case WAITING_ENTER_3TEAMS_BEFORE -> checkEnter3TeamsBeforeTrigger(scheduledNotification);
        };
    }

    /**
     * 입장 시작 트리거 조건 체크 정책
     * 정책: 예상 입장 시간이 현재 시간을 지났는가?
     */
    private boolean checkEnterNowTrigger(ScheduledNotification scheduledNotification) {
        try {
            // 알림에서 웨이팅 정보 추출
            Waiting waiting = extractWaitingFromNotification(scheduledNotification);
            if (waiting == null) return false;

            // 예상 입장 시간 계산
            LocalDateTime estimatedEnterTime = calculateEstimatedEnterTime(waiting);

            // 현재 시간이 예상 입장 시간을 지났는지 확인
            boolean triggered = LocalDateTime.now().isAfter(estimatedEnterTime);

            if (triggered) {
                log.debug("입장 시작 트리거 조건 만족 - 웨이팅 ID: {}, 예상 입장 시간: {}", waiting.id(), estimatedEnterTime);
            }

            return triggered;

        } catch (Exception e) {
            log.error("입장 시작 트리거 조건 체크 실패", e);
            return false;
        }
    }

    /**
     * 입장 시간 초과 트리거 조건 체크 정책
     * 정책: 예상 입장 시간 + 5분이 현재 시간을 지났는가?
     */
    private boolean checkEnterTimeOverTrigger(ScheduledNotification scheduledNotification) {
        try {
            Waiting waiting = extractWaitingFromNotification(scheduledNotification);
            if (waiting == null) return false;

            // 예상 입장 시간 + 5분 계산
            LocalDateTime estimatedEnterTime = calculateEstimatedEnterTime(waiting);
            LocalDateTime timeOverThreshold = estimatedEnterTime.plusMinutes(5);

            boolean triggered = LocalDateTime.now().isAfter(timeOverThreshold);

            if (triggered) {
                log.debug("입장 시간 초과 트리거 조건 만족 - 웨이팅 ID: {}, 초과 기준 시간: {}", waiting.id(), timeOverThreshold);
            }

            return triggered;

        } catch (Exception e) {
            log.error("입장 시간 초과 트리거 조건 체크 실패", e);
            return false;
        }
    }

    /**
     * 리뷰 요청 트리거 조건 체크 정책
     * 정책: 예상 입장 시간 + 2시간이 현재 시간을 지났는가?
     */
    private boolean checkReviewRequestTrigger(ScheduledNotification scheduledNotification) {
        try {
            Waiting waiting = extractWaitingFromNotification(scheduledNotification);
            if (waiting == null) return false;

            // 예상 입장 시간 + 2시간 계산
            LocalDateTime estimatedEnterTime = calculateEstimatedEnterTime(waiting);
            LocalDateTime reviewRequestTime = estimatedEnterTime.plusHours(2);

            boolean triggered = LocalDateTime.now().isAfter(reviewRequestTime);

            if (triggered) {
                log.debug("리뷰 요청 트리거 조건 만족 - 웨이팅 ID: {}, 리뷰 요청 시간: {}", waiting.id(), reviewRequestTime);
            }

            return triggered;

        } catch (Exception e) {
            log.error("리뷰 요청 트리거 조건 체크 실패", e);
            return false;
        }
    }

    /**
     * 3팀 전 알림 트리거 조건 체크 정책
     * 정책: 현재 웨이팅이 4번째 순번(앞에 3팀)에 도달했는가?
     */
    private boolean checkEnter3TeamsBeforeTrigger(ScheduledNotification scheduledNotification) {
        try {
            Waiting waiting = extractWaitingFromNotification(scheduledNotification);
            if (waiting == null) return false;

            // 현재 실제 대기 순번 계산
            int currentPosition = calculateCurrentWaitingPosition(waiting);

            // 4번째 순번(앞에 3팀)에 도달했는지 확인
            boolean triggered = currentPosition <= 4;

            if (triggered) {
                log.debug("3팀 전 알림 트리거 조건 만족 - 웨이팅 ID: {}, 현재 순번: {}", waiting.id(), currentPosition);
            }

            return triggered;

        } catch (Exception e) {
            log.error("3팀 전 알림 트리거 조건 체크 실패", e);
            return false;
        }
    }

    /**
     * 스케줄된 알림 실제 발송
     */
    private void sendScheduledNotification(ScheduledNotification scheduledNotification) {
        try {
            Notification notification = scheduledNotification.getReservatedNotification();

            // 실제 알림 저장 (발송)
            notificationPort.save(notification);

            // 처리 완료 표시 (TODO: ScheduledNotification에 처리 완료 상태 추가 필요)
            markAsProcessed(scheduledNotification);

            log.info("스케줄된 알림 발송 완료 - 멤버 ID: {}, 트리거: {}",
                    notification.getMember().id(),
                    scheduledNotification.getScheduledNotificationTrigger());

        } catch (Exception e) {
            log.error("스케줄된 알림 발송 실패", e);
        }
    }

    // === 유틸리티 메서드들 ===

    /**
     * 알림에서 웨이팅 정보 추출
     */
    private Waiting extractWaitingFromNotification(ScheduledNotification scheduledNotification) {
        // TODO: 실제 구현에서는 알림의 도메인 이벤트에서 웨이팅 ID를 추출하여 조회
        // 현재는 임시 구현
        return null;
    }

    /**
     * 예상 입장 시간 계산 (WaitingNotificationService와 동일한 로직)
     */
    private LocalDateTime calculateEstimatedEnterTime(Waiting waiting) {
        int teamsAhead = waiting.waitingNumber() - 1;
        int estimatedWaitMinutes = teamsAhead * 15; // 팀당 15분
        return waiting.registeredAt().plusMinutes(estimatedWaitMinutes);
    }

    /**
     * 현재 실제 대기 순번 계산
     */
    private int calculateCurrentWaitingPosition(Waiting waiting) {
        // TODO: 실제 구현에서는 해당 팝업의 현재 대기 중인 웨이팅들을 조회하여 순번 계산
        // 현재는 임시 구현
        return Integer.MAX_VALUE;
    }

    /**
     * 미처리 스케줄된 알림들 조회
     */
    private List<ScheduledNotification> getPendingScheduledNotifications() {
        ScheduledNotificationQuery query = new ScheduledNotificationQuery();
        return scheduledNotificationPort.findAllByQuery(query);
    }

    /**
     * 스케줄된 알림을 처리 완료로 표시
     */
    private void markAsProcessed(ScheduledNotification scheduledNotification) {
        // TODO: ScheduledNotification에 처리 상태 필드 추가 후 구현
        log.debug("스케줄된 알림 처리 완료 표시");
    }
} 