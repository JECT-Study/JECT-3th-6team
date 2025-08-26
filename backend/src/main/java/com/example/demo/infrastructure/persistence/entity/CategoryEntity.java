package com.example.demo.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * 카테고리 엔티티.
 * 카테고리 기본 정보를 저장한다.
 */
@Entity
@Table(name = "categories")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
