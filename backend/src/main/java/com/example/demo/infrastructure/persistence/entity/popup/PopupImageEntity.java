package com.example.demo.infrastructure.persistence.entity.popup;

import jakarta.persistence.*;
import lombok.*;

/**
 * 팝업 이미지 엔티티.
 * 팝업의 메인 및 설명 이미지를 저장한다.
 */
@Entity
@Table(name = "popup_images")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "popup_id", nullable = false)
    private Long popupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PopupImageType type;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}

