package com.example.demo.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Bans")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = true)
    private Long popupId; // null이면 전역 제재

    @Column(nullable = false)
    private LocalDateTime startAt; // 제재 시작일

    @Column(nullable = false)
    private LocalDateTime endAt; // 제재 종료일
}
