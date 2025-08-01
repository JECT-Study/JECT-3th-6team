package com.example.demo.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    // 파라미터 검증 관련
    PARAMETER_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "PARAMETER_VALIDATION_FAILED", "요청 파라미터 검증에 실패했습니다"),
    
    // 페이지네이션 관련 - NotificationService.java:56, 59
    INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "INVALID_PAGE_SIZE", "페이지 크기가 유효하지 않습니다"),
    
    // 정렬/필터 관련 - NotificationService.java:72, 84
    INVALID_READ_STATUS(HttpStatus.BAD_REQUEST, "INVALID_READ_STATUS", "유효하지 않은 읽음 상태입니다"),
    INVALID_SORT_OPTION(HttpStatus.BAD_REQUEST, "INVALID_SORT_OPTION", "유효하지 않은 정렬 옵션입니다"),
    
    // 권한 관련 - NotificationService.java:95, 110, WaitingService.java:107
    ACCESS_DENIED_NOTIFICATION(HttpStatus.FORBIDDEN, "ACCESS_DENIED_NOTIFICATION", "해당 알림에 접근할 권한이 없습니다"),
    ACCESS_DENIED_WAITING(HttpStatus.FORBIDDEN, "ACCESS_DENIED_WAITING", "본인의 대기 정보만 조회할 수 있습니다"),
    
    // 엔티티 미존재 관련
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다"), // MemberController.java:32, NotificationPortAdapter.java:168, ScheduledNotificationPortAdapter.java:110, WaitingService.java:54
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION_NOT_FOUND", "알림을 찾을 수 없습니다"), // NotificationPortAdapter.java:61, NotificationService.java:92, 107
    POPUP_NOT_FOUND(HttpStatus.NOT_FOUND, "POPUP_NOT_FOUND", "팝업을 찾을 수 없습니다"), // PopupService.java:66, WaitingService.java:47
    WAITING_NOT_FOUND(HttpStatus.NOT_FOUND, "WAITING_NOT_FOUND", "대기 정보를 찾을 수 없습니다"), // NotificationPortAdapter.java:184, ScheduledNotificationPortAdapter.java:126, WaitingService.java:103
    
    // 도메인 검증 관련
    INVALID_WAITING_STATUS(HttpStatus.BAD_REQUEST, "INVALID_WAITING_STATUS", "유효하지 않은 대기 상태입니다"), // WaitingStatus.java:27
    INVALID_PEOPLE_COUNT(HttpStatus.BAD_REQUEST, "INVALID_PEOPLE_COUNT", "대기 인원수가 유효하지 않습니다"), // Waiting.java:43
    INVALID_WAITING_PERSON_NAME(HttpStatus.BAD_REQUEST, "INVALID_WAITING_PERSON_NAME", "대기자 이름이 유효하지 않습니다"), // Waiting.java:46, 49
    INVALID_OPENING_HOURS(HttpStatus.BAD_REQUEST, "INVALID_OPENING_HOURS", "운영 시간이 유효하지 않습니다"), // OpeningHours.java:20, WeeklyOpeningHours.java:25
    INVALID_POPUP_TYPE(HttpStatus.BAD_REQUEST, "INVALID_POPUP_TYPE", "지원하지 않는 팝업 타입입니다"), // PopupType.java:37
    
    // 데이터 무결성 관련
    NULL_NOTIFICATION_ID(HttpStatus.BAD_REQUEST, "NULL_NOTIFICATION_ID", "알림 ID가 null입니다"), // NotificationPortAdapter.java:50
    INVALID_SOURCE_DOMAIN(HttpStatus.BAD_REQUEST, "INVALID_SOURCE_DOMAIN", "유효하지 않은 소스 도메인입니다"), // NotificationPortAdapter.java:176, ScheduledNotificationPortAdapter.java:118
    UNSUPPORTED_NOTIFICATION_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "UNSUPPORTED_NOTIFICATION_TYPE", "지원하지 않는 알림 타입입니다"); // NotificationDtoMapper.java:62, NotificationPortAdapter.java:142, ScheduledNotificationPortAdapter.java:77, NotificationEntityMapper.java:123

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}