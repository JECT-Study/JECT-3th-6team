package com.example.demo.infrastructure.persistence.entity.popup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "popup_images")
public class PopupImageEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "popup_id", nullable = false)
    private Long popupId;

    // TODO: model에서 enum 도입 후 타입 교체 예정
    @Column(name = "type", nullable = false)
    private String type; // ex: MAIN, DESCRIPTION

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder; // 0, 1, 2 등으로 정렬
}

