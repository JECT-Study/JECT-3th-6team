package com.example.demo.domain.model.waiting;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.Popup;

import java.time.LocalDateTime;

/**
 * 대기 도메인 모델.
 * 팝업에 대한 대기 정보를 나타낸다.
 */
public record Waiting(
        Long id,
        Popup popup,
        String waitingPersonName,
        Member member,
        String contactEmail,
        Integer peopleCount,
        Integer waitingNumber,
        WaitingStatus status,
        LocalDateTime registeredAt,
        LocalDateTime enteredAt,
        LocalDateTime canEnterAt
) {

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
        this(id, popup, waitingPersonName, member, contactEmail, peopleCount, waitingNumber, status, registeredAt, null, null);
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
        this(id, popup, waitingPersonName, member, contactEmail, peopleCount, waitingNumber, status, registeredAt, enteredAt, null);
    }


    /**
     * 입장 처리를 수행한다.
     *
     * @return 입장 시간이 설정된 새로운 Waiting 객체
     */
    public Waiting enter() {
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
                LocalDateTime.now(),
                canEnterAt
        );
    }

    public Waiting minusWaitingNumber() {
        LocalDateTime canEnterAt = waitingNumber == 1 ? LocalDateTime.now() : null;

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
                canEnterAt
        );
    }
}