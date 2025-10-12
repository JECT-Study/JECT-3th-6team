package com.example.demo.domain.model.waiting;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 대기 조회 조건을 표현하는 클래스.
 * 커서 기반 페이지네이션과 필터링 조건을 포함한다.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public sealed class WaitingQuery {
    private final Long waitingId;
    private final Long memberId;
    private final Integer size;
    private final Long lastWaitingId;
    private final WaitingStatus status;
    private final SortOrder sortOrder;
    private final Long popupId;
    private final LocalDate date;
    private final Boolean excludeNoShow;

    public static ForWaitingId forWaitingId(Long waitingId) {
        return new ForWaitingId(waitingId);
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
    public static ForVisitHistory forVisitHistory(Long memberId, Integer size, Long lastWaitingId, String status) {
        WaitingStatus waitingStatus = WaitingStatus.fromString(status);
        return new ForVisitHistory(memberId, size, lastWaitingId, waitingStatus);
    }

    public static ForPopup forPopup(Long popupId, WaitingStatus status) {
        return new ForPopup(popupId, status);
    }

    public static ForDuplicateCheck forDuplicateCheck(Long memberId, Long popupId, LocalDate date) {
        return new ForDuplicateCheck(memberId, popupId, date);
    }

    public static ForMemberAndPopupOnDate forMemberAndPopupOnDate(Long memberId, Long popupId, LocalDate date) {
        return new ForMemberAndPopupOnDate(memberId, popupId, date);
    }

    public static ForStatus forStatus(WaitingStatus status) {
        return new ForStatus(status);
    }

    public static ForMemberAndPopupWithStatus forMemberAndPopupWithStatus(Long memberId, Long popupId, WaitingStatus status) {
        return new ForMemberAndPopupWithStatus(memberId, popupId, status);
    }

    public static ForCanEnterWaiting forCanEnterWaiting() {
        return new ForCanEnterWaiting();
    }

    /**
     * 정렬 순서를 정의하는 enum
     */
    public enum SortOrder {
        /**
         * RESERVED 상태가 먼저, 그 다음 날짜 최신순
         */
        RESERVED_FIRST_THEN_DATE_DESC,
    }

    public static final class ForWaitingId extends WaitingQuery {
        private ForWaitingId(Long waitingId, Long memberId, Integer size, Long lastWaitingId, WaitingStatus status,
                             SortOrder sortOrder, Long popupId, LocalDate date, Boolean excludeNoShow) {
            super(waitingId, memberId, size, lastWaitingId, status, sortOrder, popupId, date, excludeNoShow);
        }

        public ForWaitingId(Long waitingId) {
            this(waitingId, null, null, null, null, null, null, null, null);
        }
    }

    public static final class ForVisitHistory extends WaitingQuery {
        private ForVisitHistory(Long waitingId, Long memberId, Integer size, Long lastWaitingId, WaitingStatus status,
                                SortOrder sortOrder, Long popupId, LocalDate date, Boolean excludeNoShow) {
            super(waitingId, memberId, size, lastWaitingId, status, sortOrder, popupId, date, excludeNoShow);
        }

        public ForVisitHistory(Long memberId, Integer size, Long lastWaitingId, WaitingStatus status) {
            this(null, memberId, size, lastWaitingId, status, SortOrder.RESERVED_FIRST_THEN_DATE_DESC, null, null, null);
        }
    }

    public static final class ForPopup extends WaitingQuery {
        private ForPopup(Long waitingId, Long memberId, Integer size, Long lastWaitingId, WaitingStatus status,
                         SortOrder sortOrder, Long popupId, LocalDate date, Boolean excludeNoShow) {
            super(waitingId, memberId, size, lastWaitingId, status, sortOrder, popupId, date, excludeNoShow);
        }

        public ForPopup(Long popupId, WaitingStatus status) {
            this(null, null, null, null, status, null, popupId, null, null);
        }
    }

    public static final class ForDuplicateCheck extends WaitingQuery {
        private ForDuplicateCheck(Long waitingId, Long memberId, Integer size, Long lastWaitingId, WaitingStatus status,
                                  SortOrder sortOrder, Long popupId, LocalDate date, Boolean excludeNoShow) {
            super(waitingId, memberId, size, lastWaitingId, status, sortOrder, popupId, date, excludeNoShow);
        }

        public ForDuplicateCheck(Long memberId, Long popupId, LocalDate date) {
            this(null, memberId, null, null, null, null, popupId, date, null);
        }
    }

    public static final class ForMemberAndPopupOnDate extends WaitingQuery {
        private ForMemberAndPopupOnDate(Long waitingId, Long memberId, Integer size, Long lastWaitingId, WaitingStatus status,
                                        SortOrder sortOrder, Long popupId, LocalDate date, Boolean excludeNoShow) {
            super(waitingId, memberId, size, lastWaitingId, status, sortOrder, popupId, date, excludeNoShow);
        }

        public ForMemberAndPopupOnDate(Long memberId, Long popupId, LocalDate date) {
            this(null, memberId, null, null, null, null, popupId, date, null);
        }
    }

    public static final class ForStatus extends WaitingQuery {
        private ForStatus(Long waitingId, Long memberId, Integer size, Long lastWaitingId, WaitingStatus status,
                          SortOrder sortOrder, Long popupId, LocalDate date, Boolean excludeNoShow) {
            super(waitingId, memberId, size, lastWaitingId, status, sortOrder, popupId, date, excludeNoShow);
        }

        public ForStatus(WaitingStatus status) {
            this(null, null, null, null, status, null, null, null, null);
        }
    }

    public static final class ForMemberAndPopupWithStatus extends WaitingQuery {
        private ForMemberAndPopupWithStatus(Long waitingId, Long memberId, Integer size, Long lastWaitingId, WaitingStatus status,
                                            SortOrder sortOrder, Long popupId, LocalDate date, Boolean excludeNoShow) {
            super(waitingId, memberId, size, lastWaitingId, status, sortOrder, popupId, date, excludeNoShow);
        }

        public ForMemberAndPopupWithStatus(Long memberId, Long popupId, WaitingStatus status) {
            this(null, memberId, null, null, status, null, popupId, null, null);
        }
    }

    public static final class ForCanEnterWaiting extends WaitingQuery {
        private ForCanEnterWaiting(Long waitingId, Long memberId, Integer size, Long lastWaitingId, WaitingStatus status, SortOrder sortOrder, Long popupId, LocalDate date, Boolean excludeNoShow) {
            super(waitingId, memberId, size, lastWaitingId, status, sortOrder, popupId, date, excludeNoShow);
        }

        public ForCanEnterWaiting() {
            this(null, null, null, null, WaitingStatus.WAITING, null, null, null, false);
        }
    }
} 