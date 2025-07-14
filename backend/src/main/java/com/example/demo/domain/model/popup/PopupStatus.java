package com.example.demo.domain.model.popup;

/**
 * 팝업의 상태를 나타내는 열거형.
 */
public enum PopupStatus {
    /**
     * 팝업이 예정된 상태.
     */
    SCHEDULED,

    /**
     * 팝업이 진행 중인 상태.
     */
    IN_PROGRESS,

    /**
     * 팝업이 종료된 상태.
     */
    FINISHED
} 