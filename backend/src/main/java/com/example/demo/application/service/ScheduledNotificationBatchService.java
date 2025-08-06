package com.example.demo.application.service;

import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.notification.ScheduledNotification;
import com.example.demo.domain.model.notification.ScheduledNotificationQuery;
import com.example.demo.domain.model.notification.ScheduledNotificationTrigger;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.NotificationEventPort;
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
import java.util.Objects;

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
    private final NotificationEventPort notificationEventPort;

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


        List<ScheduledNotification> needToSend = pendingNotifications.stream().filter(this::isTriggerConditionSatisfied).toList();

        List<ScheduledNotification> completeSend = needToSend.stream()
                .map(this::sendScheduledNotification)
                .filter(Objects::nonNull)
                .toList();

        if (!completeSend.isEmpty()) {
            log.info("스케줄된 알림 배치 처리 완료 - 발송: {}개", completeSend.size());
        }

        scheduledNotificationPort.delete(completeSend);
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
     * 정책: 자신보다 선순위에 WAITING 상태인 대기자가 없는가?
     */
    private boolean checkEnterNowTrigger(ScheduledNotification scheduledNotification) {
        try {
            Waiting waiting = extractWaitingFromNotification(scheduledNotification);
            if (waiting == null) return false;

            // 자신보다 선순위에 WAITING 상태인 대기자가 있는지 확인
            int waitingCount = waitingPort.countWaitingBefore(waiting.popup().id(), waiting.waitingNumber());
            boolean triggered = waitingCount == 0; // 선순위 대기자가 없으면 입장 알림

            if (triggered) {
                log.debug("입장 시작 트리거 조건 만족 - 웨이팅 ID: {}, 현재 순번: {}", waiting.id(), waiting.waitingNumber());
            }

            return triggered;

        } catch (Exception e) {
            log.error("입장 시작 트리거 조건 체크 실패", e);
            return false;
        }
    }

    /**
     * 입장 시간 초과 트리거 조건 체크 정책
     * 정책: 입장 알림 발송 후 5분 초과 시 (아직 VISITED 상태가 아닌 경우)
     */
    private boolean checkEnterTimeOverTrigger(ScheduledNotification scheduledNotification) {
        try {
            LocalDateTime enterNotificationSentAt = scheduledNotification.getEnterNotificationSentAt();
            if (enterNotificationSentAt == null) return false; // 입장 알림이 아직 발송되지 않음

            Waiting waiting = extractWaitingFromNotification(scheduledNotification);
            if (waiting == null) return false;
            if (waiting.status() == WaitingStatus.VISITED) return false; // 이미 입장 처리됨

            LocalDateTime timeOverThreshold = enterNotificationSentAt.plusMinutes(5);
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
     * 정책: 실제 입장 처리(VISITED 상태) + 2시간 후
     */
    private boolean checkReviewRequestTrigger(ScheduledNotification scheduledNotification) {
        try {
            LocalDateTime actualEnterTime = scheduledNotification.getActualEnterTime();
            if (actualEnterTime == null) return false; // 아직 입장하지 않음

            LocalDateTime reviewRequestTime = actualEnterTime.plusHours(2);
            boolean triggered = LocalDateTime.now().isAfter(reviewRequestTime);

            if (triggered) {
                Waiting waiting = extractWaitingFromNotification(scheduledNotification);
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
     * 정책: 자신보다 선순위의 WAITING 상태 대기자가 3명 이하인가?
     */
    private boolean checkEnter3TeamsBeforeTrigger(ScheduledNotification scheduledNotification) {
        try {
            Waiting waiting = extractWaitingFromNotification(scheduledNotification);
            if (waiting == null) return false;

            // 자신보다 선순위의 WAITING 상태 대기자 수 확인
            int waitingAhead = waitingPort.countWaitingBefore(waiting.popup().id(), waiting.waitingNumber());

            // 앞에 3팀 이하가 남았는지 확인
            boolean triggered = waitingAhead <= 3;

            if (triggered) {
                log.debug("3팀 전 알림 트리거 조건 만족 - 웨이팅 ID: {}, 앞선 대기자: {}명", waiting.id(), waitingAhead);
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
    private ScheduledNotification sendScheduledNotification(ScheduledNotification scheduledNotification) {
        try {
            Notification notification = scheduledNotification.getReservatedNotification();
            Long memberId = notification.getMember().id();

            // 1. 실제 알림 저장 (DB 저장)
            Notification savedNotification = notificationPort.save(notification);

            // 2. 입장 알림인 경우 발송 시간 기록
            if (scheduledNotification.getScheduledNotificationTrigger() == ScheduledNotificationTrigger.WAITING_ENTER_NOW) {
                try {
                    scheduledNotificationPort.updateEnterNotificationSentAt(scheduledNotification.getId(), LocalDateTime.now());
                    log.debug("입장 알림 발송 시간 기록 완료 - 스케줄 ID: {}", scheduledNotification.getId());
                } catch (Exception e) {
                    log.warn("입장 알림 발송 시간 기록 실패 - 스케줄 ID: {}", scheduledNotification.getId(), e);
                }
            }

            // 3. SSE를 통한 실시간 알림 발송 (연결된 클라이언트가 있는 경우에만)
            if (notificationEventPort.isConnected(memberId)) {
                notificationEventPort.sendRealTimeNotification(memberId, savedNotification);
                log.debug("실시간 알림 발송 완료 - 멤버 ID: {}, 알림 ID: {}", memberId, savedNotification.getId());
            } else {
                log.debug("SSE 연결이 없는 회원입니다. 실시간 알림을 스킵합니다. - 멤버 ID: {}", memberId);
            }

            log.info("스케줄된 알림 발송 완료 - 멤버 ID: {}, 트리거: {}, 알림 ID: {}",
                    memberId,
                    scheduledNotification.getScheduledNotificationTrigger(),
                    savedNotification.getId());

            return scheduledNotification;

        } catch (Exception e) {
            log.error("스케줄된 알림 발송 실패 - 스케줄 ID: {}", scheduledNotification.getId(), e);
            return null;
        }
    }

    // === 유틸리티 메서드들 ===

    /**
     * 알림에서 웨이팅 정보 추출
     */
    private Waiting extractWaitingFromNotification(ScheduledNotification scheduledNotification) {
        return (Waiting) scheduledNotification.getReservatedNotification()
                .getEvent()
                .getSource();
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
        return waiting.waitingNumber();
    }

    /**
     * 미처리 스케줄된 알림들 조회
     */
    private List<ScheduledNotification> getPendingScheduledNotifications() {
        ScheduledNotificationQuery query = new ScheduledNotificationQuery();
        return scheduledNotificationPort.findAllByQuery(query);
    }
} 