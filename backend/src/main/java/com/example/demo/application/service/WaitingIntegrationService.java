package com.example.demo.application.service;

import com.example.demo.application.dto.waiting.WaitingCreateRequest;
import com.example.demo.application.dto.waiting.WaitingCreateResponse;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.domain.port.WaitingPort;
import com.example.demo.application.mapper.WaitingDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 기존 WaitingService와 새로운 알림 서비스들을 연동하는 통합 서비스.
 * 실제 운영에서는 기존 WaitingService를 이렇게 수정하면 됩니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingIntegrationService {

    private final WaitingPort waitingPort;
    private final PopupPort popupPort;
    private final WaitingDtoMapper waitingDtoMapper;
    
    // 새로운 알림 서비스 의존성 추가
    private final WaitingNotificationService waitingNotificationService;

    /**
     * 웨이팅 생성 + 새로운 알림 정책 적용
     */
    @Transactional
    public WaitingCreateResponse createWaitingWithNotifications(WaitingCreateRequest request) {
        
        // === 기존 웨이팅 생성 로직 ===
        
        // 1. 팝업 존재 여부 확인
        var popup = popupPort.findById(request.popupId())
                .orElseThrow(() -> new IllegalArgumentException("팝업을 찾을 수 없습니다: " + request.popupId()));

        // 2. 다음 대기 번호 조회
        Integer nextWaitingNumber = waitingPort.getNextWaitingNumber(request.popupId());

        // 3. 회원 정보 (임시)
        Member member = new Member(request.memberId(), "dd", "robin@naver.com");

        // 4. 대기 정보 생성
        Waiting waiting = new Waiting(
                null, // ID는 저장소에서 생성
                popup,
                request.name(),
                member,
                request.contactEmail(),
                request.peopleCount(),
                nextWaitingNumber,
                WaitingStatus.WAITING,
                LocalDateTime.now()
        );

        // 5. 대기 정보 저장
        Waiting savedWaiting = waitingPort.save(waiting);

        // === 새로운 알림 정책 적용 ===
        
        // 6. 새로운 알림 서비스로 모든 알림 처리 위임
        waitingNotificationService.processWaitingCreatedNotifications(savedWaiting);
        
        // 7. 응답 생성
        WaitingCreateResponse response = waitingDtoMapper.toCreateResponse(savedWaiting);
        
        log.info("웨이팅 생성 및 알림 처리 완료 - 웨이팅 ID: {}, 대기번호: {}", 
                savedWaiting.id(), savedWaiting.waitingNumber());
        
        return response;
    }
} 