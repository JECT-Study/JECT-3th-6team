package com.example.demo.infrastructure.persistence.entity.popup;

import jakarta.persistence.*;
import lombok.*;

/**
 * 팝업 카테고리 매핑 엔티티.
 * 팝업과 카테고리의 관계 및 이름 정보를 저장한다.
 */
@Entity
@Table(name = "popup_categories")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "popup_id", nullable = false)
    private Long popupId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "name", nullable = false)
    private String name;
}
