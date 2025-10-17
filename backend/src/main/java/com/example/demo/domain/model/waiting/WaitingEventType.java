package com.example.demo.domain.model.waiting;

public enum WaitingEventType {
    WAITING_CONFIRMED, // 대기 확정
    ENTER_3TEAMS_BEFORE, // 입장 3팀 전
    ENTER_NOW, // 입장
    ENTER_TIME_OVER, // 입장 시간 초과
    NOSHOW_FIRST, // 첫 번째 노쇼
    NOSHOW_SECOND, // 두 번째 노쇼
    NOSHOW_GLOBAL_BAN // 글로벌 밴
} 