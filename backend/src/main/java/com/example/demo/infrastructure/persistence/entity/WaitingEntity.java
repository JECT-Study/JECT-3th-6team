package com.example.demo.infrastructure.persistence.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.domain.model.waiting.WaitingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 대기 엔티티.
 * 팝업에 대한 대기 정보를 저장한다.
 */
@Entity
@Table(name = "waitings")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "popup_id", nullable = false)
    private Long popupId;

    @Column(name = "waiting_person_name", nullable = false)
    private String waitingPersonName;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Column(name = "people_count", nullable = false)
    private Integer peopleCount;

    @Column(name = "initial_waiting_number", nullable = false)
    private Integer initialWaitingNumber;

    @Column(name = "waiting_number", nullable = false)
    private Integer waitingNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WaitingStatus status;

    @Column(name = "entered_at")
    private LocalDateTime enteredAt;

    @Column(name = "can_enter_at")
    private LocalDateTime canEnterAt;
} 
