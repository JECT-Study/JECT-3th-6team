package com.example.demo.domain.model.waiting;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.Popup;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 대기 도메인 모델.
 * 팝업에 대한 대기 정보를 나타낸다.
 */
public record Waiting(
        Long id,
        Popup popup,
        String waitingPersonName,
        Member member,
        @Email(message = "대기자 이메일이 형식에 맞지 않습니다.")
        String contactEmail,
        Integer peopleCount,
        Integer waitingNumber,
        WaitingStatus status,
        LocalDateTime registeredAt,
        LocalDateTime enteredAt,
        LocalDateTime canEnterAt,
        Integer expectedWaitingTimeMinutes
) {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z가-힣0-9][a-zA-Z가-힣0-9]*$");

    /**
     * 대기 정보를 생성한다.
     *
     * @param id                대기 ID
     * @param popup             팝업 정보
     * @param waitingPersonName 대기자 이름
     * @param peopleCount       대기 인원수
     * @param contactEmail      대기자 이메일
     * @param waitingNumber     대기 번호
     * @param status            대기 상태
     * @param registeredAt      등록 시간
     * @param enteredAt         입장 시간
     * @throws IllegalArgumentException 유효하지 않은 인원수인 경우
     */
    public Waiting {
        if (peopleCount == null || peopleCount < 1 || peopleCount > 6) {
            throw new BusinessException(ErrorType.INVALID_PEOPLE_COUNT, String.valueOf(peopleCount));
        }
        if (waitingPersonName == null || waitingPersonName.length() < 2 || waitingPersonName.length() > 20) {
            throw new BusinessException(ErrorType.INVALID_WAITING_PERSON_NAME, waitingPersonName);
        }
        if (!NAME_PATTERN.matcher(waitingPersonName).matches()) {
            throw new BusinessException(ErrorType.INVALID_WAITING_PERSON_NAME, waitingPersonName);
        }
    }

    /**
     * 기존 필드만으로 대기 정보를 생성한다.
     *
     * @param id                대기 ID
     * @param popup             팝업 정보
     * @param waitingPersonName 대기자 이름
     * @param member            회원 정보
     * @param contactEmail      대기자 이메일
     * @param peopleCount       대기 인원수
     * @param waitingNumber     대기 번호
     * @param status            대기 상태
     * @param registeredAt      등록 시간
     */
    public Waiting(
            Long id,
            Popup popup,
            String waitingPersonName,
            Member member,
            String contactEmail,
            Integer peopleCount,
            Integer waitingNumber,
            WaitingStatus status,
            LocalDateTime registeredAt
    ) {
        this(id, popup, waitingPersonName, member, contactEmail, peopleCount, waitingNumber, status, registeredAt, null, null, null);
    }

    /**
     * 기존 필드만으로 대기 정보를 생성한다.
     *
     * @param id                대기 ID
     * @param popup             팝업 정보
     * @param waitingPersonName 대기자 이름
     * @param member            회원 정보
     * @param contactEmail      대기자 이메일
     * @param peopleCount       대기 인원수
     * @param waitingNumber     대기 번호
     * @param status            대기 상태
     * @param registeredAt      등록 시간
     * @param enteredAt         입장 시간
     */
    public Waiting(
            Long id,
            Popup popup,
            String waitingPersonName,
            Member member,
            String contactEmail,
            Integer peopleCount,
            Integer waitingNumber,
            WaitingStatus status,
            LocalDateTime registeredAt,
            LocalDateTime enteredAt
    ) {
        this(id, popup, waitingPersonName, member, contactEmail, peopleCount, waitingNumber, status, registeredAt, enteredAt, null, null);
    }


    /**
     * 입장 처리를 수행한다.
     *
     * @return 입장 시간이 설정된 새로운 Waiting 객체
     * @throws BusinessException 대기중 상태가 아닌 경우
     */
    public Waiting enter() {
        if (status != WaitingStatus.WAITING) {
            throw new BusinessException(ErrorType.INVALID_WAITING_STATUS, status.toString());
        }

        if (waitingNumber != 0) {
            throw new BusinessException(ErrorType.WAITING_NOT_READY, "대기 번호가 0이 아닙니다.");
        }

        LocalDateTime enteredAt = LocalDateTime.now();
        LocalDateTime canEnterAt = Optional.ofNullable(canEnterAt()).orElse(enteredAt); // canEnterAt이 null인 경우 현재 시간 사용

        return new Waiting(
                id,
                popup,
                waitingPersonName,
                member,
                contactEmail,
                peopleCount,
                waitingNumber,
                WaitingStatus.VISITED,
                registeredAt,
                enteredAt,
                canEnterAt,
                expectedWaitingTimeMinutes
        );
    }

    public Waiting minusWaitingNumber(PopupWaitingStatistics waitingStatistics) {
        if (waitingNumber == 0) {
            throw new BusinessException(ErrorType.WAITING_NOT_READY, "대기 번호는 0 이상이어야 합니다.");
        }

        if (status != WaitingStatus.WAITING) {
            throw new BusinessException(ErrorType.INVALID_WAITING_STATUS, status.toString());
        }

        return new Waiting(
                id,
                popup,
                waitingPersonName,
                member,
                contactEmail,
                peopleCount,
                waitingNumber - 1, // 대기 번호 감소
                WaitingStatus.WAITING,
                registeredAt,
                enteredAt,
                canEnterAt,
                waitingStatistics.calculateExpectedWaitingTime(waitingNumber - 1)
        );
    }

    /**
     * 노쇼 상태로 변경한다.
     *
     * @return 노쇼 상태로 변경된 새로운 Waiting 객체
     */
    public Waiting markAsNoShow() {
        if (status != WaitingStatus.WAITING) {
            throw new BusinessException(ErrorType.INVALID_WAITING_STATUS, status.toString());
        }

        return new Waiting(
                id,
                popup,
                waitingPersonName,
                member,
                contactEmail,
                peopleCount,
                waitingNumber,
                WaitingStatus.NO_SHOW,
                registeredAt,
                enteredAt,
                canEnterAt,
                expectedWaitingTimeMinutes
        );
    }

    public Waiting markAsCanEnter() {
        if (status != WaitingStatus.WAITING) {
            throw new BusinessException(ErrorType.INVALID_WAITING_STATUS, status.toString());
        }

        return new Waiting(
                id,
                popup,
                waitingPersonName,
                member,
                contactEmail,
                peopleCount,
                waitingNumber,
                WaitingStatus.WAITING,
                registeredAt,
                enteredAt,
                LocalDateTime.now(),
                expectedWaitingTimeMinutes
        );
    }
}