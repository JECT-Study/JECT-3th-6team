package com.example.demo.application.service;

import com.example.demo.application.dto.image.ImageUploadResponse;
import com.example.demo.application.dto.popup.PopupCreateRequest;
import com.example.demo.application.dto.popup.PopupCreateResponse;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.config.AppProperties;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.popup.PopupQuery;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.domain.port.WaitingPort;
import com.example.demo.domain.port.WaitingStatisticsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AppProperties appProperties;
    private final PopupPort popupPort;
    private final WaitingPort waitingPort;
    private final WaitingStatisticsPort waitingStatisticsPort;
    private final AdminSessionService adminSessionService;
    private final PopupService popupService;
    private final ImageService imageService;
    private final WaitingNotificationService waitingNotificationService;

    /**
     * 관리자 비밀번호 검증 및 세션 토큰 발급
     */
    public String loginAndCreateSession(String password) {
        if (password == null || password.isBlank()) {
            throw new BusinessException(ErrorType.ADMIN_PASSWORD_REQUIRED);
        }

        String adminPassword = System.getenv("ADMIN_PASSWORD");
        if (adminPassword == null) {
            adminPassword = appProperties.getAdminPassword();
        }

        if (!password.equals(adminPassword)) {
            throw new BusinessException(ErrorType.INVALID_ADMIN_PASSWORD);
        }

        // 비밀번호 검증 성공 시 세션 토큰 발급
        return adminSessionService.createSession();
    }

    /**
     * 세션 토큰 검증
     */
    public void verifySession(String token) {
        if (!adminSessionService.validateSession(token)) {
            throw new BusinessException(ErrorType.INVALID_ADMIN_PASSWORD, "유효하지 않거나 만료된 세션입니다");
        }
    }

    /**
     * 로그아웃 (세션 삭제)
     */
    public void logout(String token) {
        adminSessionService.deleteSession(token);
    }

    /**
     * 팝업 목록 조회 (관리자용)
     */
    @Transactional(readOnly = true)
    public List<AdminPopupSummary> getAllPopups() {
        PopupQuery query = new PopupQuery(null, 1000, List.of(), List.of(), null, null, null, null, null);
        List<Popup> popups = popupPort.findByQuery(query);
        
        return popups.stream()
                .map(popup -> new AdminPopupSummary(
                        popup.getId(),
                        popup.getName(),
                        popup.getType() != null ? popup.getType().name() : null,
                        popup.getSchedule().dateRange().startDate(),
                        popup.getSchedule().dateRange().endDate(),
                        popup.getLocation().addressName()
                ))
                .toList();
    }
    
    public record AdminPopupSummary(
            Long id,
            String name,
            String type,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate,
            String address
    ) {}
    
    public record AdminWaitingSummary(
            Long id,
            Integer waitingNumber,
            String waitingPersonName,
            Integer peopleCount,
            String contactEmail,
            java.time.LocalDateTime registeredAt
    ) {}

    /**
     * 팝업 삭제
     */
    @Transactional
    public void deletePopup(Long popupId) {
        Popup popup = popupPort.findById(popupId)
                .orElseThrow(() -> new BusinessException(ErrorType.POPUP_NOT_FOUND, String.valueOf(popupId)));

        popupPort.deleteById(popupId);
        log.info("팝업 삭제 완료: popupId={}, name={}", popupId, popup.getName());
    }

    /**
     * 특정 팝업의 대기 목록 조회 (페이지네이션)
     */
    @Transactional(readOnly = true)
    public List<AdminWaitingSummary> getWaitingsByPopup(Long popupId, int page, int size) {
        // 팝업 존재 확인
        popupPort.findById(popupId)
                .orElseThrow(() -> new BusinessException(ErrorType.POPUP_NOT_FOUND, String.valueOf(popupId)));

        WaitingQuery query = WaitingQuery.forPopup(popupId, WaitingStatus.WAITING);

        List<Waiting> allWaitings = waitingPort.findByQuery(query);

        // 수동 페이지네이션
        int start = page * size;
        int end = Math.min(start + size, allWaitings.size());

        if (start >= allWaitings.size()) {
            return List.of();
        }

        return allWaitings.subList(start, end).stream()
                .map(waiting -> new AdminWaitingSummary(
                        waiting.id(),
                        waiting.waitingNumber(),
                        waiting.waitingPersonName(),
                        waiting.peopleCount(),
                        waiting.contactEmail(),
                        waiting.registeredAt()
                ))
                .toList();
    }

    /**
     * 대기 입장 처리 (맨 앞 순번만 가능)
     */
    @Transactional
    public void enterWaiting(Long waitingId) {
        Waiting waiting = waitingPort.findByQuery(WaitingQuery.forWaitingId(waitingId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorType.WAITING_NOT_FOUND, String.valueOf(waitingId)));

        // 대기 번호가 0번인지 확인
        if (waiting.waitingNumber() != 0) {
            throw new BusinessException(ErrorType.WAITING_NOT_READY);
        }

        // 상태가 WAITING인지 확인
        if (waiting.status() != WaitingStatus.WAITING) {
            throw new BusinessException(ErrorType.INVALID_WAITING_STATUS, waiting.status().name());
        }

        // 입장 처리
        Waiting enteredWaiting = waiting.enter();
        waitingPort.save(enteredWaiting);

        // 나머지 대기자들의 번호 감소
        decrementWaitingNumbers(waiting.popup().getId());

        log.info("대기 입장 처리 완료: waitingId={}, member={}", waitingId, waiting.member().name());
    }

    /**
     * 대기 번호 감소 (입장 처리 후)
     * N+1 문제 방지를 위해 배치 저장 사용
     */
    private void decrementWaitingNumbers(Long popupId) {
        WaitingQuery query = WaitingQuery.forPopup(popupId, WaitingStatus.WAITING);
        List<Waiting> waitings = waitingPort.findByQuery(query);
        
        // 팝업 통계 조회
        var statistics = waitingStatisticsPort.findCompletedStatisticsByPopupId(popupId);
        Double avgTimePerPerson = statistics.calculateAverageTimePerPerson();
        log.info("[입장 처리] 예상 대기 시간 업데이트 시작 - popupId: {}, 평균 대기시간: {}분/팀, 대기자 수: {}", 
                popupId, avgTimePerPerson, waitings.size());

        Waiting newFirstWaiting = null;
        Waiting new3rdWaiting = null;
        List<Waiting> decrementedWaitings = new ArrayList<>();

        // 대기 중인 모든 대기자의 번호를 1씩 감소하고 리스트에 모음
        for (Waiting waiting : waitings) {
            if (waiting.waitingNumber() > 0) {
                Waiting decremented = waiting.minusWaitingNumber(statistics);
                decrementedWaitings.add(decremented);
                
                log.debug("[입장 처리] 예상 대기 시간 업데이트 - waitingId: {}, 대기번호: {}번->{}번, 예상시간: {}분", 
                        waiting.id(), waiting.waitingNumber(), decremented.waitingNumber(), 
                        decremented.expectedWaitingTimeMinutes());
                
                // 새로 0번이 된 사람 (기존 1번)
                if (decremented.waitingNumber() == 0) {
                    newFirstWaiting = decremented;
                } 
                // 새로 3번이 된 사람 (기존 4번)
                else if (decremented.waitingNumber() == 3) {
                    new3rdWaiting = decremented;
                }
            }
        }
        
        // 배치로 한 번에 저장 (N+1 문제 해결)
        if (!decrementedWaitings.isEmpty()) {
            waitingPort.saveAll(decrementedWaitings);
            log.info("[입장 처리] 예상 대기 시간 업데이트 완료 - {} 건 일괄 저장", decrementedWaitings.size());
        }
        
        // 새로 0번이 된 사람에게 입장 알림 발송 (SSE + 이메일)
        if (newFirstWaiting != null) {
            waitingNotificationService.sendEnterNowNotification(newFirstWaiting);
            log.info("새로운 0번 대기자에게 입장 알림 발송: waitingId={}, memberId={}", 
                    newFirstWaiting.id(), newFirstWaiting.member().id());
        }
        
        // 새로 3번이 된 사람에게 3팀 전 알림 발송 (SSE)
        if (new3rdWaiting != null) {
            waitingNotificationService.sendEnter3TeamsBeforeNotification(new3rdWaiting);
            log.info("3팀 전 알림 발송: waitingId={}, memberId={}", 
                    new3rdWaiting.id(), new3rdWaiting.member().id());
        }
    }

    /**
     * 특정 팝업의 전체 대기 수 조회
     */
    @Transactional(readOnly = true)
    public int getTotalWaitingCount(Long popupId) {
        WaitingQuery query = WaitingQuery.forPopup(popupId, WaitingStatus.WAITING);

        return waitingPort.findByQuery(query).size();
    }

    /**
     * 팝업 생성 (관리자 전용)
     */
    @Transactional
    public PopupCreateResponse createPopup(PopupCreateRequest request) {
        PopupCreateResponse response = popupService.create(request);
        log.info("관리자가 팝업 생성 완료: popupId={}", response.popupId());
        return response;
    }

    /**
     * 이미지 업로드 (관리자 전용)
     */
    public ImageUploadResponse uploadImage(MultipartFile file) {
        ImageUploadResponse response = imageService.uploadImage(file);
        log.info("관리자가 이미지 업로드 완료: url={}", response.imageUrl());
        return response;
    }
}

