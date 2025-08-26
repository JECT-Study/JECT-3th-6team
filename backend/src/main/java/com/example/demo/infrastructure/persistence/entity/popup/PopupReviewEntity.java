package com.example.demo.infrastructure.persistence.entity.popup;

import com.example.demo.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 팝업 리뷰 엔티티.
 * 사용자 리뷰, 평점, 내용 등을 저장한다.
 */
@Entity
@Table(name = "popup_reviews")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "popup_id", nullable = false)
    private Long popupId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;
}