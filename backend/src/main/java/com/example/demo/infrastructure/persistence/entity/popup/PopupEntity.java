package com.example.demo.infrastructure.persistence.entity.popup;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import lombok.Getter;

@Entity
@Table(name = "popups")
@Getter
public class PopupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "popup_location_id", nullable = false)
    private Long popupLocationId;

    @Column(name = "slot_interval_minutes", nullable = false)
    private int slotIntervalMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PopupType type;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;


}
