package com.example.demo.infrastructure.persistence.entity.popup;

import com.example.demo.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "popups")
public class PopupEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slot_interval_minutes", nullable = false)
    private Integer slotIntervalMinutes;

    // TODO: model에서 enum 도입 후 타입 교체 예정
    @Column(name = "type", nullable = false)
    private String type; // ex: EXPERIENTIAL, PRE_ORDER

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;


}
