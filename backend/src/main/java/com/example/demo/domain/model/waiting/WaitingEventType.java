package com.example.demo.domain.model.waiting;

public enum WaitingEventType {
    WAITING_CONFIRMED, // 대기 확정
    ENTER_3TEAMS_BEFORE, // 입장 3팀 전
    ENTER_NOW, // 입장
    ENTER_TIME_OVER, // 입장 시간 초과
    REVIEW_REQUEST // 리뷰 요청
} 