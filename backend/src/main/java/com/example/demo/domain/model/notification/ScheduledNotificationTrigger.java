package com.example.demo.domain.model.notification;

/*
 * 작명 규칙 : {도메인 이름}_{트리거 구분 이름}
 */
public enum ScheduledNotificationTrigger {
    WAITING_ENTER_3TEAMS_BEFORE, // 입장 3팀 전
    WAITING_ENTER_NOW, // 입장
    WAITING_ENTER_TIME_OVER, // 입장 시간 초과
    WAITING_REVIEW_REQUEST // 리뷰 요청
}
