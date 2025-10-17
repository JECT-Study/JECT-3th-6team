package com.example.demo.application.service;

import com.example.demo.application.dto.waiting.VisitHistoryCursorResponse;
import com.example.demo.application.dto.waiting.WaitingCreateRequest;
import com.example.demo.application.dto.waiting.WaitingCreateResponse;
import com.example.demo.application.dto.waiting.WaitingResponse;
import com.example.demo.application.mapper.WaitingDtoMapper;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.ban.BanQuery;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingPort waitingPort;
    private final PopupPort popupPort;
    private final MemberPort memberPort;
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
    public WaitingCreateResponse createWaiting(WaitingCreateRequest request, LocalDateTime requestTime) {
        // 1. 팝업 존재 여부 확인
        var popup = popupPort.findById(request.popupId())
                .orElseThrow(() -> new BusinessException(ErrorType.POPUP_NOT_FOUND, String.valueOf(request.popupId())));

        // 팝업이 운영 중인지 확인
        if (!popup.isOpenAt(requestTime)) {
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
                WaitingQuery.forMemberAndPopupOnDate(request.memberId(), request.popupId(), requestTime.toLocalDate())
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
                requestTime,
                null,
                null,
                expectedWaitingTime,
                nextWaitingNumber
        );


        // 5. 대기 정보 저장
        Waiting savedWaiting = waitingPort.save(waiting);

        // 7. 확인 알림 발송
        waitingNotificationService.sendWaitingConfirmedNotification(savedWaiting);

        // 8. 응답 생성
        return waitingDtoMapper.toCreateResponse(savedWaiting);
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

            // 해당 팝업의 대기중인 팀 수 조회
            int waitingCount = waitingPort.findByQuery(WaitingQuery.forPopup(waiting.popup().getId(), WaitingStatus.WAITING)).size();
            WaitingResponse waitingResponse = waitingDtoMapper.toResponse(waiting, waitingCount);
            return new VisitHistoryCursorResponse(List.of(waitingResponse), waitingId, false);
        }

        // 1. 조회 조건 생성
        WaitingQuery query = WaitingQuery.forVisitHistory(memberId, size, lastWaitingId, status);

        // 2. 대기 내역 조회
        List<Waiting> waitings = waitingPort.findByQuery(query);

        // 3. 다음 페이지 존재 여부 확인
        boolean hasNext = waitings.size() == size;
        Long lastId = waitings.isEmpty() ? null : waitings.getLast().id();

        // 4. DTO 변환 (각 팝업별 대기중인 팀 수 포함)
        List<WaitingResponse> waitingResponses = waitings.stream()
                .map(waiting -> {
                    // 해당 팝업의 대기중인 팀 수 조회
                    int waitingCount = waitingPort.findByQuery(WaitingQuery.forPopup(waiting.popup().getId(), WaitingStatus.WAITING)).size();
                    return waitingDtoMapper.toResponse(waiting, waitingCount);
                })
                .toList();

        return new VisitHistoryCursorResponse(waitingResponses, lastId, hasNext);
    }

    /**
     * 대기열 입장 처리 (관리자용)
     * 0번 대기자만 입장 가능하며, 입장 후 나머지 대기자들의 번호를 감소시킨다.
     *
     * @param waitingId 입장 처리할 대기 ID
     * @throws BusinessException 대기 정보를 찾을 수 없거나, 입장 조건을 만족하지 않는 경우
     */
    @Transactional
    public void enterWaiting(Long waitingId) {
        // 1. 대기 정보 조회
        Waiting waiting = waitingPort.findByQuery(WaitingQuery.forWaitingId(waitingId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorType.WAITING_NOT_FOUND, String.valueOf(waitingId)));

        // 2. 대기 번호 검증 (0번만 입장 가능)
        if (waiting.waitingNumber() != 0) {
            throw new BusinessException(ErrorType.WAITING_NOT_READY);
        }

        // 3. 상태 검증 (WAITING 상태만 입장 가능)
        if (waiting.status() != WaitingStatus.WAITING) {
            throw new BusinessException(ErrorType.INVALID_WAITING_STATUS, waiting.status().name());
        }

        // 4. 입장 처리
        Waiting enteredWaiting = waiting.enter();
        waitingPort.save(enteredWaiting);

        // 5. 나머지 대기자들의 번호 감소 및 예상 시간 업데이트
        decrementWaitingNumbers(waiting.popup().getId());
    }

    /**
     * 특정 팝업의 대기 순번을 감소시키고 예상 대기시간을 업데이트한다.
     * N+1 문제 방지를 위해 배치 저장 사용
     *
     * @param popupId 팝업 ID
     */
    private void decrementWaitingNumbers(Long popupId) {
        // 1. 해당 팝업의 모든 대기중인 대기 조회
        WaitingQuery query = WaitingQuery.forPopup(popupId, WaitingStatus.WAITING);
        List<Waiting> waitings = waitingPort.findByQuery(query);

        // 2. 팝업 통계 조회
        var statistics = waitingStatisticsPort.findCompletedStatisticsByPopupId(popupId);
        Double avgTimePerPerson = statistics.calculateAverageTimePerPerson();

        log.info("[입장 처리] 예상 대기 시간 업데이트 시작 - popupId: {}, 평균 대기시간: {}분/팀, 대기자 수: {}",
                popupId, avgTimePerPerson, waitings.size());

        Waiting newFirstWaiting = null;
        Waiting new3rdWaiting = null;
        List<Waiting> decrementedWaitings = new java.util.ArrayList<>();

        // 3. 대기 중인 모든 대기자의 번호를 1씩 감소
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

        // 4. 배치로 한 번에 저장 (N+1 문제 해결)
        if (!decrementedWaitings.isEmpty()) {
            waitingPort.saveAll(decrementedWaitings);
            log.info("[입장 처리] 예상 대기 시간 업데이트 완료 - {} 건 일괄 저장", decrementedWaitings.size());
        }

        // 5. 새로 0번이 된 사람에게 입장 알림 발송 (SSE + 이메일)
        if (newFirstWaiting != null) {
            waitingNotificationService.sendEnterNowNotification(newFirstWaiting);
        }

        // 6. 새로 3번이 된 사람에게 3팀 전 알림 발송 (SSE)
        if (new3rdWaiting != null) {
            waitingNotificationService.sendEnter3TeamsBeforeNotification(new3rdWaiting);
        }
    }
} 
