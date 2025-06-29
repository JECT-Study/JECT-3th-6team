package com.example.demo.infrastructure.persistence.entity.popup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "popup_contents")
@Getter
public class PopupContentEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "popup_id", nullable = false)
    private Long popupId;

    @Column(name = "content_text", columnDefinition = "TEXT", nullable = false)
    private String contentText;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}

