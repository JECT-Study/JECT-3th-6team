package com.example.demo.application.service;

import com.example.demo.application.dto.VisitHistoryCursorResponse;
import com.example.demo.application.dto.WaitingCreateRequest;
import com.example.demo.application.dto.WaitingCreateResponse;
import com.example.demo.application.dto.WaitingResponse;
import com.example.demo.application.mapper.WaitingDtoMapper;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.domain.port.WaitingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingPort waitingPort;
    private final PopupPort popupPort;
    private final MemberPort memberPort;
    private final WaitingDtoMapper waitingDtoMapper;

    /**
     * 현장 대기 신청
     */
    @Transactional
    public WaitingCreateResponse createWaiting(WaitingCreateRequest request) {
        // 1. 팝업 존재 여부 확인
        var popup = popupPort.findById(request.popupId())
                .orElseThrow(() -> new IllegalArgumentException("팝업을 찾을 수 없습니다: " + request.popupId()));

        // 2. 다음 대기 번호 조회
        Integer nextWaitingNumber = waitingPort.getNextWaitingNumber(request.popupId());

        // 3. 회원 정보 조회
//        Member member = memberPort.findById(request.memberId())
//                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + request.memberId()));

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
                WaitingStatus.RESERVED,
                LocalDateTime.now()
        );

        // 5. 대기 정보 저장
        Waiting savedWaiting = waitingPort.save(waiting);

        // 6. 응답 생성
        return waitingDtoMapper.toCreateResponse(savedWaiting);
    }

    /**
     * 내 방문/예약 내역 조회 (무한 스크롤)
     */
    @Transactional(readOnly = true)
    public VisitHistoryCursorResponse getVisitHistory(Integer size, Long lastWaitingId, String status) {
        // TODO: 사용자 인증 확인 (현재는 임시로 memberId = 1 사용)
        Long memberId = 1L;

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
} 
