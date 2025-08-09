package com.example.demo.application.dto.notification;

import java.time.LocalDateTime;

/**
 * 웨이팅 입장 알림 이메일 발송 요청 DTO
 */
public record WaitingEntryNotificationRequest(
    String storeName,           // 스토어명
    String memberName,          // 대기자명 (고객 이름)
    int waitingCount,           // 대기자 수
    String memberEmail,         // 대기자 이메일
    LocalDateTime waitingDateTime, // 대기 일자
    String storeLocation        // 매장 위치
) {
}
