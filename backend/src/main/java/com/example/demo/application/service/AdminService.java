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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AppProperties appProperties;
    private final PopupPort popupPort;
    private final WaitingPort waitingPort;
    private final AdminSessionService adminSessionService;
    private final PopupService popupService;
    private final ImageService imageService;
    private final WaitingService waitingService;

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
     * 대기 입장 처리 (관리자용)
     * 실제 입장 처리 로직은 WaitingService에 위임
     */
    @Transactional
    public void enterWaiting(Long waitingId) {
        // WaitingService에 위임 (도메인 로직 재사용)
        waitingService.enterWaiting(waitingId);
        
        log.info("[Admin] 대기 입장 처리 완료: waitingId={}", waitingId);
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

