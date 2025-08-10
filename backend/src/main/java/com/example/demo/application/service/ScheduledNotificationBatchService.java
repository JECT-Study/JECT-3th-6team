package com.example.demo.application.service;

import com.example.demo.application.dto.notification.WaitingEntryNotificationRequest;
import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.notification.ScheduledNotification;
import com.example.demo.domain.model.notification.ScheduledNotificationQuery;
import com.example.demo.domain.model.notification.ScheduledNotificationTrigger;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingEventType;
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
    private final EmailNotificationService emailNotificationService;

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
            case WAITING_ENTER_3TEAMS_BEFORE -> checkEnter3TeamsBeforeTrigger(scheduledNotification);
        };
    }

    /**
     * 입장 시작 트리거 조건 체크 정책
     * 정책: 현재 입장이 가능한 순번인가?
     */
    private boolean checkEnterNowTrigger(ScheduledNotification scheduledNotification) {
        try {
            // 알림에서 웨이팅 정보 추출
            Waiting waiting = extractWaitingFromNotification(scheduledNotification);
            return waiting != null && waiting.waitingNumber() == 0 && waiting.status() == WaitingStatus.WAITING;
        } catch (Exception e) {
            log.error("입장 시작 트리거 조건 체크 실패", e);
            return false;
        }
    }

    /**
     * 입장 시간 초과 트리거 조건 체크 정책
     * 정책: 입장 가능 시간 + 5분이 현재 시간을 지났는가?
     */
    private boolean checkEnterTimeOverTrigger(ScheduledNotification scheduledNotification) {
        try {
            Waiting waiting = extractWaitingFromNotification(scheduledNotification);
            if (waiting == null) return false;

            LocalDateTime canEnterAt = waiting.canEnterAt();

            return canEnterAt.plusMinutes(5).isBefore(LocalDateTime.now());
        } catch (Exception e) {
            log.error("입장 시간 초과 트리거 조건 체크 실패", e);
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
    private ScheduledNotification sendScheduledNotification(ScheduledNotification scheduledNotification) {
        try {
            Notification notification = scheduledNotification.getReservatedNotification();
            Long memberId = notification.getMember().id();

            // 1. 실제 알림 저장 (DB 저장)
            Notification savedNotification = notificationPort.save(notification);

            // 2. SSE를 통한 실시간 알림 발송 (연결된 클라이언트가 있는 경우에만)
            if (notificationEventPort.isConnected(memberId)) {
                notificationEventPort.sendRealTimeNotification(memberId, savedNotification);
                log.debug("실시간 알림 발송 완료 - 멤버 ID: {}, 알림 ID: {}", memberId, savedNotification.getId());
            } else {
                log.debug("SSE 연결이 없는 회원입니다. 실시간 알림을 스킵합니다. - 멤버 ID: {}", memberId);
            }
            // 3. 이메일 알림이 필요한 경우 발송
            // TODO : 이메일 알림 정책이 일반 알림 정책과 다른 부분 고려해 리팩토링
            String eventType = notification.getEvent().getEventType();

            if (eventType.equals(WaitingEventType.ENTER_NOW.name())) {
                Waiting waiting = extractWaitingFromNotification(scheduledNotification);
                sendEntryEmailNotification(waiting);
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

    /**
     * 입장 알림 이메일 발송
     */
    private void sendEntryEmailNotification(Waiting waiting) {
        try {
            // Waiting에서 필요한 정보 추출
            var popup = waiting.popup();
            var location = popup.getLocation();

            // 매장 위치 링크 생성 (카카오맵)
            String storeLocation = generateMapLink(location);

            // 이메일 발송 요청 DTO 생성
            var request = new WaitingEntryNotificationRequest(
                    popup.getName(),                    // 스토어명
                    waiting.waitingPersonName(),        // 대기자명
                    waiting.peopleCount(),              // 대기자 수
                    waiting.contactEmail(),             // 대기자 이메일
                    waiting.registeredAt(),             // 대기 일자
                    storeLocation                       // 매장 위치 링크
            );

            // 비동기로 이메일 발송
            emailNotificationService.sendWaitingEntryNotificationAsync(request);

            log.info("입장 알림 이메일 발송 요청 완료 - 웨이팅 ID: {}, 수신자: {}",
                    waiting.id(), waiting.contactEmail());

        } catch (Exception e) {
            log.error("입장 알림 이메일 발송 실패 - 웨이팅 ID: {}", waiting.id(), e);
        }
    }

    /**
     * 매장 위치 링크 생성 (카카오맵)
     */
    private String generateMapLink(com.example.demo.domain.model.Location location) {
        return String.format("https://map.kakao.com/link/map/%s,%s,%s",
                location.addressName(),
                location.latitude(),
                location.longitude()
        );
    }
} 