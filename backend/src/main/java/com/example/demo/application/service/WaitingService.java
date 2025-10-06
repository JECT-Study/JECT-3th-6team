package com.example.demo.application.service;

import com.example.demo.application.dto.waiting.*;
import com.example.demo.application.mapper.WaitingDtoMapper;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.ban.BanQuery;
import com.example.demo.domain.model.waiting.PopupWaitingStatistics;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingPort waitingPort;
    private final PopupPort popupPort;
    private final MemberPort memberPort;
    private final NotificationPort notificationPort;
    private final WaitingDtoMapper waitingDtoMapper;
    private final WaitingNotificationService waitingNotificationService;
    private final BanPort banPort;
    private final WaitingStatisticsPort waitingStatisticsPort;

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

        // 팝업이 운영 중인지 확인
        LocalDateTime now = LocalDateTime.now();
        if (!popup.isOpenAt(now)) {
            throw new BusinessException(ErrorType.POPUP_NOT_OPENED);
        }

        // 제재 여부 확인
        boolean notPopupBan = banPort.findByQuery(BanQuery.byMemberAndPopup(request.memberId(), request.popupId())).isEmpty();
        boolean notGlobalBan = banPort.findByQuery(BanQuery.byMemberIdFromAll(request.memberId())).isEmpty();

        if (!notPopupBan || !notGlobalBan) {
            throw new BusinessException(ErrorType.BANNED_MEMBER, String.valueOf(request.memberId()));
        }

        // 그날 해당 팝업에 예약한 적 있는지 확인
        // 노쇼 1개만 있는 경우는 재신청 허용
        List<Waiting> todayWaitings = waitingPort.findByQuery(
                WaitingQuery.forMemberAndPopupOnDate(request.memberId(), request.popupId(), now.toLocalDate())
        );

        // 노쇼가 아닌 예약이 있는지 확인
        boolean hasActiveWaiting = todayWaitings.stream()
                .anyMatch(w -> w.status() != WaitingStatus.NO_SHOW);

        // 노쇼 개수 확인
        long noShowCount = todayWaitings.stream()
                .filter(w -> w.status() == WaitingStatus.NO_SHOW)
                .count();

        // 활성 예약이 있거나, 노쇼가 2개 이상이면 중복 신청 불가
        if (hasActiveWaiting || noShowCount >= 2) {
            throw new BusinessException(ErrorType.DUPLICATE_WAITING, String.valueOf(request.popupId()));
        }

        // 2. 다음 대기 번호 조회
        Integer nextWaitingNumber = waitingPort.getNextWaitingNumber(request.popupId());

        // 3. 회원 정보 조회
        Member member = memberPort.findById(request.memberId())
                .orElseThrow(() -> new BusinessException(ErrorType.MEMBER_NOT_FOUND, String.valueOf(request.memberId())));

        Integer expectedWaitingTime = waitingStatisticsPort.findCompletedStatisticsByPopupId(request.popupId())
                .calculateExpectedWaitingTime(nextWaitingNumber);

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
                now,
                null,
                null,
                expectedWaitingTime
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
            Waiting waiting = waitingPort.findByQuery(WaitingQuery.forWaitingId(waitingId))
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
     * 대기열 입장 처리
     */
    @Transactional
    public void makeVisit(WaitingMakeVisitRequest request) {
        // 1. 대기 정보 조회
        WaitingQuery query = WaitingQuery.forWaitingId(request.waitingId());
        Waiting waiting = waitingPort.findByQuery(query)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorType.WAITING_NOT_FOUND, String.valueOf(request.waitingId())));

        if (waiting.waitingNumber() != 0) {
            throw new BusinessException(ErrorType.WAITING_NOT_READY, String.valueOf(request.waitingId()));
        }
        // 2. 입장 처리
        Waiting enteredWaiting = waiting.enter();

        // 3. 입장 처리된 대기 저장
        waitingPort.save(enteredWaiting);

        // 5. 같은 팝업의 나머지 대기자들 순번 앞당기기 및 예상 대기시간 업데이트
        reorderWaitingNumbersAndUpdateExpectedTime(waiting.popup().getId());
    }

    /**
     * 특정 팝업의 대기 순번을 앞당기고 예상 대기시간을 업데이트한다.
     *
     * @param popupId 팝업 ID
     */
    private void reorderWaitingNumbersAndUpdateExpectedTime(Long popupId) {
        // 1. 해당 팝업의 모든 대기중인 대기 조회
        WaitingQuery popupQuery = WaitingQuery.forPopup(popupId, WaitingStatus.WAITING);
        List<Waiting> waitingList = waitingPort.findByQuery(popupQuery);

        // 2. 최신 통계 데이터 조회
        PopupWaitingStatistics updatedStatistics = waitingStatisticsPort.findCompletedStatisticsByPopupId(popupId);

        // 3. 순번 앞당기기
        List<Waiting> reorderedWaitings = waitingList.stream()
                .filter(w -> w.waitingNumber() > 0)
                .map(waiting -> waiting.minusWaitingNumber(updatedStatistics))
                .toList();

        // 4. 배치로 저장
        waitingPort.saveAll(reorderedWaitings);
    }
} 
