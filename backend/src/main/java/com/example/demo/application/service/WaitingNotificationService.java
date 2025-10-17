package com.example.demo.application.service;

import com.example.demo.application.dto.notification.WaitingEntryNotificationRequest;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingDomainEvent;
import com.example.demo.domain.model.waiting.WaitingEventType;
import com.example.demo.domain.port.NotificationEventPort;
import com.example.demo.domain.port.NotificationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 웨이팅 관련 알림 정책을 관리하는 서비스.
 * 알림 생성 규칙과 스케줄링 로직을 담당한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingNotificationService {

    private final NotificationPort notificationPort;
    private final NotificationEventPort notificationEventPort;
    private final EmailNotificationService emailNotificationService;

    // === 알림 정책 상수 ===
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM.dd");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("E");

    /**
     * 웨이팅 확정 알림 즉시 발송 (WAITING_CONFIRMED 정책)
     */
    @Transactional
    public void sendWaitingConfirmedNotification(Waiting waiting) {
        log.info("웨이팅 확정 알림 발송 - 팝업 ID: {}, 대기 ID: {}, 회원 ID: {}", waiting.popup().getId(), waiting.id(), waiting.member().id());
        String content = generateWaitingConfirmedContent(waiting);

        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.WAITING_CONFIRMED);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content(content)
                .build();

        sendNotification(notification);
    }

    /**
     * 알림 발송 공통 로직
     */
    private void sendNotification(Notification notification) {
        try {
            // 1. 알림 저장
            Notification savedNotification = notificationPort.save(notification);

            // 2. 실시간 알림 발송 (SSE 연결이 있는 경우)
            if (notificationEventPort.isConnected(notification.getMember().id())) {
                notificationEventPort.sendRealTimeNotification(notification.getMember().id(), savedNotification);
                log.debug("알림 발송 완료 - 멤버 ID: {}", notification.getMember().id());
            } else {
                log.debug("SSE 연결이 없는 회원입니다. 실시간 알림을 스킵합니다. - 멤버 ID: {}", notification.getMember().id());
            }

        } catch (Exception e) {
            log.error("알림 발송 실패 - 멤버 ID: {}", notification.getMember().id(), e);
        }
    }

    /**
     * 노쇼 처리 시 알림 정책 실행.
     * 노쇼 횟수에 따라 다른 알림 발송
     */
    @Transactional
    public void processNoShowNotifications(Waiting waiting, long noShowCount) {
        if (noShowCount == 1) {
            // 첫 번째 노쇼
            sendFirstNoShowNotification(waiting);
        } else if (noShowCount == 2) {
            // 두 번째 노쇼
            sendSecondNoShowNotification(waiting);
        }
    }

    /**
     * 글로벌 밴 알림 발송
     */
    @Transactional
    public void sendGlobalBanNotification(Long memberId) {
        log.info("글로벌 밴 알림 발송 - 회원 ID: {}", memberId);
        // todo: 정책 확정 시 반영
        String content = "누적 노쇼로 인해 3일간 모든 팝업 예약이 제한됩니다.";

        // 글로벌 밴 알림은 대기 정보가 없으므로 간단한 알림 생성
        WaitingDomainEvent event = new WaitingDomainEvent(null, WaitingEventType.NOSHOW_GLOBAL_BAN);
        Notification notification = Notification.builder()
                .member(new Member(memberId, null, null))
                .event(event)
                .content(content)
                .build();

        sendNotification(notification);
    }

    /**
     * 첫 번째 노쇼 알림 발송
     */
    private void sendFirstNoShowNotification(Waiting waiting) {
        log.info("첫 번째 노쇼 알림 발송 - 대기 ID: {}, 회원 ID: {}", waiting.id(), waiting.member().id());

        String content = """
                입장 시간 10분 초과로 미방문 처리되었습니다. 해당 팝업의 웨이팅 기회가 1회 남았어요.
                """;

        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.NOSHOW_FIRST);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content(content)
                .build();

        sendNotification(notification);
    }

    /**
     * 두 번째 노쇼 알림 발송
     */
    private void sendSecondNoShowNotification(Waiting waiting) {
        log.info("두 번째 노쇼 알림 발송 - 대기 ID: {}, 회원 ID: {}", waiting.id(), waiting.member().id());
        // todo: 정책 확정 시 반영
        String content = """
                오늘의 모든 웨이팅 기회를 소진하여 해당 팝업에 대한 이용이 제한되었어요. 내일 다시 이용해주세요!
                """;

        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.NOSHOW_SECOND);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content(content)
                .build();

        sendNotification(notification);
    }

    /**
     * 새로운 0번이 된 대기자에게 입장 알림 발송
     */
    @Transactional
    public void sendEnterNowNotification(Waiting waiting) {
        log.info("입장 가능 알림 발송 - 대기 ID: {}, 회원 ID: {}", waiting.id(), waiting.member().id());

        String content = "지금 매장으로 입장 부탁드립니다. 즐거운 시간 보내세요!";

        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.ENTER_NOW);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content(content)
                .build();

        sendNotification(notification);

        // 이메일 알림도 발송
        sendEntryEmailNotification(waiting);
    }

    /**
     * 3팀 전 알림 발송
     */
    @Transactional
    public void sendEnter3TeamsBeforeNotification(Waiting waiting) {
        log.info("3팀 전 알림 발송 - 대기 ID: {}, 회원 ID: {}", waiting.id(), waiting.member().id());

        String content = "앞으로 3팀 남았습니다! 순서가 다가오니 매장 근처에서 대기해주세요!";

        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.ENTER_3TEAMS_BEFORE);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content(content)
                .build();

        sendNotification(notification);
    }

    /**
     * 입장 시간 초과 알림 발송
     */
    @Transactional
    public void sendEnterTimeOverNotification(Waiting waiting) {
        log.info("입장 시간 초과 알림 발송 - 대기 ID: {}, 회원 ID: {}", waiting.id(), waiting.member().id());

        String content = "입장 시간이 초과되었습니다. 빠른 입장 부탁드립니다! 입장이 지연될 경우 웨이팅이 취소될 수 있습니다.";

        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.ENTER_TIME_OVER);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content(content)
                .build();

        sendNotification(notification);
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

        } catch (Exception e) {
            log.error("입장 이메일 알림 발송 실패 - 대기 ID: {}", waiting.id(), e);
        }
    }

    /**
     * 매장 위치 링크 생성 (카카오맵)
     */
    private String generateMapLink(Location location) {
        return String.format("https://map.kakao.com/link/map/%s,%s,%s",
                location.addressName(),
                location.latitude(),
                location.longitude()
        );
    }

    /**
     * 웨이팅 확정 알림 내용 생성 정책
     * 정책: "MM.dd (요일) N인 웨이팅이 완료되었습니다. 현재 대기 번호를 확인해주세요!"
     */
    private String generateWaitingConfirmedContent(Waiting waiting) {
        LocalDateTime registeredAt = waiting.registeredAt();
        String dateText = registeredAt.format(DATE_FORMATTER);
        String dayText = registeredAt.format(DAY_FORMATTER);
        int peopleCount = waiting.peopleCount();

        return String.format("%s (%s) %d인 웨이팅이 완료되었습니다. 현재 대기 번호를 확인해주세요!",
                dateText, dayText, peopleCount);
    }
}
