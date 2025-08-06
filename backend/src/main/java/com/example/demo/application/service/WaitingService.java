package com.example.demo.application.service;

import com.example.demo.application.dto.waiting.VisitHistoryCursorResponse;
import com.example.demo.application.dto.waiting.WaitingCreateRequest;
import com.example.demo.application.dto.waiting.WaitingCreateResponse;
import com.example.demo.application.dto.waiting.WaitingResponse;
import com.example.demo.application.mapper.WaitingDtoMapper;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.domain.port.NotificationPort;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.domain.port.ScheduledNotificationPort;
import com.example.demo.domain.port.WaitingPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.example.demo.domain.model.CursorResult;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingPort waitingPort;
    private final PopupPort popupPort;
    private final MemberPort memberPort;
    private final NotificationPort notificationPort;
    private final ScheduledNotificationPort scheduledNotificationPort;
    private final WaitingDtoMapper waitingDtoMapper;
    private final WaitingNotificationService waitingNotificationService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM.dd");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("E");

    /**
     * 현장 대기 신청
     */
    @Transactional
    public WaitingCreateResponse createWaiting(WaitingCreateRequest request) {
        // 1. 팝업 존재 여부 확인
        var popup = popupPort.findById(request.popupId())
                .orElseThrow(() -> new BusinessException(ErrorType.POPUP_NOT_FOUND, String.valueOf(request.popupId())));

        // 2. 다음 대기 번호 조회
        Integer nextWaitingNumber = waitingPort.getNextWaitingNumber(request.popupId());

        // 3. 회원 정보 조회
        Member member = memberPort.findById(request.memberId())
                .orElseThrow(() -> new BusinessException(ErrorType.MEMBER_NOT_FOUND, String.valueOf(request.memberId())));

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

        // 7. 새로운 알림 서비스로 모든 알림 처리 위임
        waitingNotificationService.processWaitingCreatedNotifications(savedWaiting);

        // 8. 응답 생성
        return waitingDtoMapper.toCreateResponse(savedWaiting);
    }

    /**
     * 웨이팅 확정 알림 내용 생성
     */
    private String generateWaitingConfirmedContent(Waiting waiting) {
        LocalDateTime registeredAt = waiting.registeredAt();
        String dateText = registeredAt.format(DATE_FORMATTER);
        String dayText = registeredAt.format(DAY_FORMATTER);
        int peopleCount = waiting.peopleCount();

        return String.format("%s (%s) %d인 웨이팅이 완료되었습니다. 현재 대기 번호를 확인해주세요!",
                dateText, dayText, peopleCount);
    }

    /**
     * 내 방문/예약 내역 조회 (무한 스크롤) 또는 단건 조회
     */
    @Transactional(readOnly = true)
    public VisitHistoryCursorResponse getVisitHistory(Long memberId, Integer size, Long lastWaitingId, String status, Long waitingId) {
        
        // waitingId가 있으면 단건 조회
        if (waitingId != null) {
            Waiting waiting = waitingPort.findByQuery(new WaitingQuery(waitingId, null, null, null, null, null))
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorType.WAITING_NOT_FOUND, String.valueOf(waitingId)));

            // 본인의 대기 정보인지 확인
            if (!waiting.member().id().equals(memberId)) {
                throw new BusinessException(ErrorType.ACCESS_DENIED_WAITING, String.valueOf(waitingId));
            }

            WaitingResponse waitingResponse = waitingDtoMapper.toResponse(waiting);
            return new VisitHistoryCursorResponse(List.of(waitingResponse), waitingId, false);
        }

        // 1. 조회 조건 생성
        WaitingQuery query = WaitingQuery.forVisitHistory(memberId, size, lastWaitingId, status);

        // 2. 대기 내역 조회
        List<Waiting> waitings = waitingPort.findByQuery(query);

        // 3. 다음 페이지 존재 여부 확인
        boolean hasNext = waitings.size() == size;
        Long lastId = waitings.isEmpty() ? null : waitings.getLast().id();

        // 4. DTO 변환
        List<WaitingResponse> waitingResponses = waitings.stream()
                .map(waitingDtoMapper::toResponse)
                .toList();

        return new VisitHistoryCursorResponse(waitingResponses, lastId, hasNext);
    }

    /**
     * 입장 처리 - 웨이팅 상태를 VISITED로 변경하고 실제 입장 시간을 기록
     *
     * @param waitingId 웨이팅 ID
     * @param memberId 회원 ID (권한 확인용)
     */
    @Transactional
    public void processWaitingEnter(Long waitingId, Long memberId) {
        // 1. 웨이팅 조회 및 권한 확인
        Waiting waiting = waitingPort.findByQuery(new WaitingQuery(waitingId, null, null, null, null, null))
            .stream().findFirst()
            .orElseThrow(() -> new BusinessException(ErrorType.WAITING_NOT_FOUND, String.valueOf(waitingId)));

        if (!waiting.member().id().equals(memberId)) {
            throw new BusinessException(ErrorType.ACCESS_DENIED_WAITING, String.valueOf(waitingId));
        }

        // 2. 이미 입장 처리된 경우 무시
        if (waiting.status() == WaitingStatus.VISITED) {
            log.debug("이미 입장 처리된 웨이팅입니다 - 웨이팅 ID: {}", waitingId);
            return;
        }

        // 3. 상태를 VISITED로 변경 및 입장 시간 기록
        LocalDateTime now = LocalDateTime.now();
        Waiting updatedWaiting = new Waiting(
            waiting.id(), waiting.popup(), waiting.waitingPersonName(), waiting.member(),
            waiting.contactEmail(), waiting.peopleCount(), waiting.waitingNumber(),
            WaitingStatus.VISITED, waiting.registeredAt(),
            waiting.enterNotificationSentAt(), now // actualEnterTime 설정
        );

        waitingPort.save(updatedWaiting);

        // 4. 해당 웨이팅의 스케줄된 알림들의 실제 입장 시간 업데이트
        try {
            scheduledNotificationPort.updateActualEnterTime(waitingId, now);
            log.info("입장 처리 완료 - 웨이팅 ID: {}, 멤버 ID: {}, 입장 시간: {}", waitingId, memberId, now);
        } catch (Exception e) {
            log.error("스케줄된 알림 업데이트 실패 - 웨이팅 ID: {}", waitingId, e);
            // 입장 처리는 성공했으므로 예외를 던지지 않음
        }
    }
} 
