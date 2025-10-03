package com.example.demo.domain.model.waiting;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 대기 조회 조건을 표현하는 클래스.
 * 커서 기반 페이지네이션과 필터링 조건을 포함한다.
 */
@Builder
public record WaitingQuery(
        Long waitingId,
        Long memberId,
        Integer size,
        Long lastWaitingId,
        WaitingStatus status,
        SortOrder sortOrder,
        Long popupId,
        LocalDate date
) {

    /**
     * 정렬 순서를 정의하는 enum
     */
    public enum SortOrder {
        /**
         * RESERVED 상태가 먼저, 그 다음 날짜 최신순
         */
        RESERVED_FIRST_THEN_DATE_DESC,
    }

    /**
     * 첫 페이지 조회용 생성자 (기본 정렬: RESERVED_FIRST_THEN_DATE_DESC)
     */
    public static WaitingQuery firstPage(Long memberId, Integer size) {
        return new WaitingQuery(null, memberId, size, null, null, SortOrder.RESERVED_FIRST_THEN_DATE_DESC, null, null);
    }

    /**
     * 상태 필터링이 포함된 조회용 생성자 (기본 정렬: RESERVED_FIRST_THEN_DATE_DESC)
     */
    public static WaitingQuery withStatus(Long memberId, Integer size, WaitingStatus status) {
        return new WaitingQuery(null, memberId, size, null, status, SortOrder.RESERVED_FIRST_THEN_DATE_DESC, null, null);
    }

    /**
     * 방문 내역 조회를 위한 조회 조건을 생성한다.
     *
     * @param memberId      회원 ID
     * @param size          조회할 개수
     * @param lastWaitingId 마지막 대기 ID (첫 페이지 조회 시 null)
     * @param status        상태 문자열 (필터링 시 사용, null이면 전체 조회)
     * @return WaitingQuery 객체
     * @throws IllegalArgumentException 유효하지 않은 상태인 경우
     */
    public static WaitingQuery forVisitHistory(Long memberId, Integer size, Long lastWaitingId, String status) {
        return Optional.ofNullable(lastWaitingId)
                .map(id -> new WaitingQuery(null, memberId, size, id, WaitingStatus.fromString(status), SortOrder.RESERVED_FIRST_THEN_DATE_DESC, null, null))
                .orElseGet(() -> Optional.ofNullable(WaitingStatus.fromString(status))
                        .map(waitingStatus -> withStatus(memberId, size, waitingStatus))
                        .orElse(firstPage(memberId, size))
                );
    }

    public static WaitingQuery forWaitingId(Long waitingId) {
        return new WaitingQuery(waitingId, null, null, null, null, null, null, null);
    }

    /**
     * 팝업별 대기 조회용 생성자
     */
    public static WaitingQuery forPopup(Long popupId, WaitingStatus status) {
        return new WaitingQuery(null, null, null, null, status, null, popupId, null);
    }

    public WaitingQuery(Long waitingId, Long memberId, Integer size, Long lastWaitingId, WaitingStatus status, SortOrder sortOrder) {
        this(waitingId, memberId, size, lastWaitingId, status, sortOrder, null, null);
    }

    public static WaitingQuery forDuplicateCheck(Long memberId, Long popupId, LocalDate date) {
        return new WaitingQuery(null, memberId, null, null, null, null, popupId, date);
    }

    /**
     * 상태별 조회용 생성자
     */
    public static WaitingQuery forStatus(WaitingStatus status) {
        return new WaitingQuery(null, null, null, null, status, null, null, null);
    }
} 