package com.example.demo.domain.model;

import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * 대기 도메인 모델.
 * 팝업에 대한 대기 정보를 나타낸다.
 */
public record Waiting(
        Long id,
        com.example.demo.domain.model.popup.Popup popup,
        String waitingPersonName,
        Member member,
        @Email(message = "대기자 이메일이 형식에 맞지 않습니다.")
        String contactEmail,
        Integer peopleCount,
        Integer waitingNumber,
        WaitingStatus status,
        LocalDateTime registeredAt
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
     * @throws IllegalArgumentException 유효하지 않은 인원수인 경우
     */
    public Waiting {
        if (peopleCount == null || peopleCount < 1 || peopleCount > 6) {
            throw new IllegalArgumentException("대기 인원수는 1명 이상 6명 이하여야 합니다. 현재: " + peopleCount);
        }
        if (waitingPersonName == null || waitingPersonName.length() < 2 || waitingPersonName.length() > 20) {
            throw new IllegalArgumentException("대기자 이름은 2글자 이상 20글자 이하여야 합니다. 현재: " + waitingPersonName);
        }
        if (!NAME_PATTERN.matcher(waitingPersonName).matches()) {
            throw new IllegalArgumentException("대기자 이름에 특수문자는 포함될 수 없습니다. 현재: " + waitingPersonName);
        }
    }
}