package com.example.demo.infrastructure.persistence.entity.popup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "popup_images")
@Getter
public class PopupImageEntity{

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

