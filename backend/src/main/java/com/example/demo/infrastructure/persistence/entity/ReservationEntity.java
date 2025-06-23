package com.example.demo.infrastructure.persistence.entity;

import com.example.demo.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="reservations")
public class ReservationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "popup_id", nullable = false)
    private Long popupId;

    @Column(name = "popup_time_slot_id", nullable = false)
    private Long popupTimeSlotId;

    @Column(name = "reserver_name", nullable = false)
    private String reserverName;

    @Column(name = "number_of_people", nullable = false)
    private int numberOfPeople;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;
}