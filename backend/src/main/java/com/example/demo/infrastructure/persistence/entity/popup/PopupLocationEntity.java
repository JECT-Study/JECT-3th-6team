package com.example.demo.infrastructure.persistence.entity.popup;

import jakarta.persistence.*;
import lombok.*;

/**
 * 팝업 위치 엔티티.
 * 팝업의 주소 및 좌표 정보를 저장한다.
 */
@Entity
@Table(name = "popup_locations")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupLocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "address_name", nullable = false)
    private String addressName;

    @Column(name = "region_1depth_name", nullable = false)
    private String region1DepthName;

    @Column(name = "region_2depth_name", nullable = false)
    private String region2DepthName;

    @Column(name = "region_3depth_name")
    private String region3DepthName;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude", nullable = false)
    private Double latitude;
}

