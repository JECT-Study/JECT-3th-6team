package com.example.demo.infrastructure.persistence.entity.popup;

import jakarta.persistence.*;
import lombok.*;

/**
 * 팝업 소셜 링크 엔티티.
 * 팝업과 연결된 SNS 아이콘 URL 및 링크 URL 정보를 저장한다.
 */
@Entity
@Table(name = "popup_socials")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupSocialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "popup_id", nullable = false)
    private Long popupId;

    @Column(name = "icon_url", nullable = false)
    private String iconUrl;

    @Column(name = "link_url", nullable = false)
    private String linkUrl;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}
