package com.example.demo.infrastructure.persistence.entity.popup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "popup_review_images")
public class PopupReviewImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "popup_review_id", nullable = false)
    private Long popupReviewId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PopupImageType type;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}