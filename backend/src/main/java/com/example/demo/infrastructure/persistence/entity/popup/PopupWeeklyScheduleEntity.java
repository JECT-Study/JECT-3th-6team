package com.example.demo.infrastructure.persistence.entity.popup;

import com.example.demo.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "popup_weekly_schedules")
public class PopupWeeklyScheduleEntity extends BaseEntity {
    // 요일별 오픈시간 + 마감시간을 갖고 있는 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "popup_id", nullable = false)
    private Long popupId;

    // TODO: model에서 enum 도입 후 타입 교체 예정
    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;
}
