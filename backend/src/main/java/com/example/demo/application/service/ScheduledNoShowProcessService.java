package com.example.demo.application.service;

import com.example.demo.domain.model.ban.Ban;
import com.example.demo.domain.model.ban.BanQuery;
import com.example.demo.domain.model.ban.BanType;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.BanPort;
import com.example.demo.domain.port.WaitingPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 노쇼 처리를 위한 스케줄러 서비스.
 * 30초마다 노쇼 대상을 찾아서 처리한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledNoShowProcessService {

    private final WaitingPort waitingPort;
    private final WaitingNotificationService waitingNotificationService;
    private final BanPort banPort;

    /**
     * 30초마다 노쇼 대상을 찾아서 처리
     */
    @Scheduled(fixedDelay = 30_000)
    @Transactional
    public void processNoShows() {
        log.debug("노쇼 처리 스케줄러 시작: {}", LocalDateTime.now());

        // 1. 10분 초과된 대기자들 조회
        List<Waiting> noShowTargets = findNoShowTargets();

        if (noShowTargets.isEmpty()) {
            log.debug("노쇼 대상자가 없습니다.");
            return;
        }

        log.info("노쇼 처리 대상: {}명", noShowTargets.size());

        // 2. 각 대상자에 대해 노쇼 처리
        noShowTargets.forEach(waiting -> {
            try {
                processNoShow(waiting);
            } catch (Exception e) {
                log.error("노쇼 처리 실패 - 대기 ID: {}", waiting.id(), e);
            }
        });

        log.debug("노쇼 처리 스케줄러 완료: {}", LocalDateTime.now());
    }

    /**
     * 10분 초과된 대기자들을 조회한다.
     */
    private List<Waiting> findNoShowTargets() {
        // canEnterAt이 현재 시간보다 10분 이상 이전이고, WAITING 상태인 대기자들
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        // 모든 WAITING 상태 대기자 조회 후 필터링
        WaitingQuery query = WaitingQuery.forStatus(WaitingStatus.WAITING);
        List<Waiting> allWaiting = waitingPort.findByQuery(query);

        return allWaiting.stream()
                .filter(waiting -> waiting.canEnterAt() != null)
                .filter(waiting -> waiting.canEnterAt().isBefore(tenMinutesAgo))
                .toList();
    }

    /**
     * 개별 대기자에 대한 노쇼 처리를 수행한다.
     */
    private void processNoShow(Waiting waiting) {
        log.info("노쇼 처리 시작 - 대기 ID: {}, 회원 ID: {}, 팝업 ID: {}",
                waiting.id(), waiting.member().id(), waiting.popup().getId());

        // 1. 노쇼 상태로 변경
        Waiting noShowWaiting = waiting.markAsNoShow();
        waitingPort.save(noShowWaiting);

        // 2. 순번 재정렬
        reorderWaitingNumbers(waiting.popup().getId());

        // 3. 노쇼 알림 발송
        long noShowCount = getNoShowCountForToday(waiting.member().id(), waiting.popup().getId());
        waitingNotificationService.processNoShowNotifications(waiting, noShowCount);

        // 4. 스토어 밴
        storeBanIfNeed(waiting, noShowCount);

        globalBanIfNeed(waiting);
        log.info("노쇼 처리 완료 - 대기 ID: {}", waiting.id());
    }

    private void storeBanIfNeed(Waiting waiting, long noShowCount) {
        if (noShowCount >= 2) {
            banPort.save(
                    Ban.builder()
                            .bannedAt(LocalDateTime.now())
                            .durationDays(1)
                            .member(waiting.member())
                            .popup(waiting.popup())
                            .type(BanType.STORE)
                            .build()
            );
            log.info(
                    "스토어 밴 적용 - 회원 ID: {}, 팝업 ID: {}, 노쇼 횟수: {}",
                    waiting.member().id(),
                    waiting.popup().getId(),
                    noShowCount
            );
        }
    }

    private void globalBanIfNeed(Waiting waiting) {
        List<Ban> globalBanHistory = banPort.findByQuery(new BanQuery(
                waiting.member().id(),
                BanType.GLOBAL,
                null,
                null,
                false
        ));

        LocalDateTime lastGlobalBannedAt = globalBanHistory.stream()
                .filter(ban -> !ban.isActive())
                .map(Ban::getBannedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        int banCount = banPort.findByQuery(new BanQuery(
                        waiting.member().id(),
                        BanType.STORE,
                        null,
                        lastGlobalBannedAt,
                        false
                )
        ).size();
        if (banCount >= 10) {
            banPort.save(
                    Ban.builder()
                            .bannedAt(LocalDateTime.now())
                            .durationDays(3)
                            .member(waiting.member())
                            .type(BanType.GLOBAL)
                            .build()
            );
            log.info(
                    "글로벌 밴 적용 - 회원 ID: {}, 밴 기간: {} ~ {}",
                    waiting.member().id(), LocalDate.now(), LocalDate.now().plusDays(3)
            );

            waitingNotificationService.sendGlobalBanNotification(waiting.member().id());
        }
    }

    /**
     * 순번을 재정렬한다.
     */
    private void reorderWaitingNumbers(Long popupId) {
        // 해당 팝업의 모든 WAITING 상태 대기자 조회
        WaitingQuery query = WaitingQuery.forPopup(popupId, WaitingStatus.WAITING);
        List<Waiting> waitingList = waitingPort.findByQuery(query);

        if (waitingList.isEmpty()) {
            return;
        }

        // 대기번호 순으로 정렬
        List<Waiting> sortedWaitings = waitingList.stream()
                .sorted((w1, w2) -> w1.waitingNumber().compareTo(w2.waitingNumber()))
                .toList();

        // 순번 재정렬 (0번부터 순차적으로)
        for (int i = 0; i < sortedWaitings.size(); i++) {
            Waiting waiting = sortedWaitings.get(i);

            // 대기 번호와 canEnterAt을 업데이트한 새로운 Waiting 객체 생성
            Waiting updatedWaiting = waiting.minusWaitingNumber();

            waitingPort.save(updatedWaiting);

            // 새로운 순번에 따른 알림 발송
            switch (i) {
                case 0 -> sendEnterNowNotification(updatedWaiting);
                case 3 -> sendEnter3TeamsBeforeNotification(updatedWaiting);
            }

            // TODO: 예상 대기 시간 업데이트 로직 구현 필요
            // - 앞의 대기팀 수 × 5분으로 예상 대기 시간 계산
            // - 5팀 이상일 시엔 계산식 활용
            // - 각 대기자의 estimatedWaitingTime 필드 업데이트
        }
    }

    /**
     * 오늘 같은 팝업에서의 노쇼 개수를 조회한다.
     */
    private long getNoShowCountForToday(Long memberId, Long popupId) {
        LocalDate today = LocalDate.now();

        WaitingQuery query = WaitingQuery.builder()
                .memberId(memberId)
                .popupId(popupId)
                .status(WaitingStatus.NO_SHOW)
                .build();

        List<Waiting> noShows = waitingPort.findByQuery(query);

        return noShows.stream()
                .filter(waiting -> waiting.registeredAt().toLocalDate().equals(today))
                .count();
    }

    /**
     * 새로운 0번이 된 대기자에게 입장 알림을 발송한다.
     */
    private void sendEnterNowNotification(Waiting waiting) {
        log.info("새로운 0번 입장 알림 발송 - 대기 ID: {}, 회원 ID: {}",
                waiting.id(), waiting.member().id());

        try {
            // WaitingNotificationService의 알림 발송 로직 활용
            waitingNotificationService.sendEnterNowNotification(waiting);
        } catch (Exception e) {
            log.error("입장 알림 발송 실패 - 대기 ID: {}", waiting.id(), e);
        }
    }

    /**
     * 3팀 전 알림을 발송한다.
     */
    private void sendEnter3TeamsBeforeNotification(Waiting waiting) {
        log.info("3팀 전 알림 발송 - 대기 ID: {}, 회원 ID: {}",
                waiting.id(), waiting.member().id());

        try {
            // WaitingNotificationService의 3팀 전 알림 발송 로직 활용
            waitingNotificationService.sendEnter3TeamsBeforeNotification(waiting);
        } catch (Exception e) {
            log.error("3팀 전 알림 발송 실패 - 대기 ID: {}", waiting.id(), e);
        }
    }
}