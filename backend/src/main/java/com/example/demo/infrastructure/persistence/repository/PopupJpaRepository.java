package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.model.popup.PopupType;
import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PopupEntity를 위한 Spring Data JPA Repository.
 */
@Repository
public interface PopupJpaRepository extends JpaRepository<PopupEntity, Long> {

    /**
     * 좌표 범위로 팝업을 조회한다 (기본 메서드)
     */
    @Query("""
            SELECT p FROM PopupEntity p, PopupLocationEntity pl 
            WHERE p.popupLocationId = pl.id 
            AND pl.latitude BETWEEN :minLatitude AND :maxLatitude 
            AND pl.longitude BETWEEN :minLongitude AND :maxLongitude
            """)
    List<PopupEntity> findByCoordinateRange(
            @Param("minLatitude") java.math.BigDecimal minLatitude,
            @Param("maxLatitude") java.math.BigDecimal maxLatitude,
            @Param("minLongitude") java.math.BigDecimal minLongitude,
            @Param("maxLongitude") java.math.BigDecimal maxLongitude
    );

    /**
     * 좌표 범위와 팝업 타입으로 팝업을 조회한다
     */
    @Query("""
            SELECT p FROM PopupEntity p, PopupLocationEntity pl 
            WHERE p.popupLocationId = pl.id 
            AND pl.latitude BETWEEN :minLatitude AND :maxLatitude 
            AND pl.longitude BETWEEN :minLongitude AND :maxLongitude
            AND p.type in :types
            """)
    List<PopupEntity> findByCoordinateRangeAndType(
            @Param("minLatitude") java.math.BigDecimal minLatitude,
            @Param("maxLatitude") java.math.BigDecimal maxLatitude,
            @Param("minLongitude") java.math.BigDecimal minLongitude,
            @Param("maxLongitude") java.math.BigDecimal maxLongitude,
            @Param("types") List<PopupType> types
    );

    /**
     * 좌표 범위와 날짜 범위로 팝업을 조회한다
     */
    @Query("""
            SELECT p FROM PopupEntity p, PopupLocationEntity pl 
            WHERE p.popupLocationId = pl.id 
            AND pl.latitude BETWEEN :minLatitude AND :maxLatitude 
            AND pl.longitude BETWEEN :minLongitude AND :maxLongitude
            AND p.startDate <= :endDate AND p.endDate >= :startDate
            """)
    List<PopupEntity> findByCoordinateRangeAndDateRange(
            @Param("minLatitude") java.math.BigDecimal minLatitude,
            @Param("maxLatitude") java.math.BigDecimal maxLatitude,
            @Param("minLongitude") java.math.BigDecimal minLongitude,
            @Param("maxLongitude") java.math.BigDecimal maxLongitude,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 좌표 범위, 팝업 타입, 날짜 범위로 팝업을 조회한다
     */
    @Query("""
            SELECT p FROM PopupEntity p, PopupLocationEntity pl 
            WHERE p.popupLocationId = pl.id 
            AND pl.latitude BETWEEN :minLatitude AND :maxLatitude 
            AND pl.longitude BETWEEN :minLongitude AND :maxLongitude
            AND p.type in :types
            AND p.startDate <= :endDate AND p.endDate >= :startDate
            """)
    List<PopupEntity> findByCoordinateRangeAndTypeAndDateRange(
            @Param("minLatitude") java.math.BigDecimal minLatitude,
            @Param("maxLatitude") java.math.BigDecimal maxLatitude,
            @Param("minLongitude") java.math.BigDecimal minLongitude,
            @Param("maxLongitude") java.math.BigDecimal maxLongitude,
            @Param("types") List<PopupType> types,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * ID로 팝업을 조회한다.
     *
     * @param id 팝업 ID
     * @return 팝업 엔티티
     */
    Optional<PopupEntity> findById(Long id);

    @Query("""
        SELECT p FROM PopupEntity p
        WHERE (:popupId IS NULL OR p.id = :popupId)
        AND ((CAST(:startDate AS date) IS NULL OR CAST(:endDate  AS date) IS NULL) OR (p.endDate >= :startDate AND p.startDate <= :endDate))
        AND (:types IS NULL OR p.type IN :types)
        AND EXISTS (
            SELECT 1 FROM PopupCategoryEntity c
            WHERE c.popupId = p.id AND (:categories IS NULL OR c.name IN :categories)
        )
        AND EXISTS (
            SELECT 1 FROM PopupLocationEntity l
            WHERE l.id = p.popupLocationId AND (:region1DepthName IS NULL OR l.region1DepthName = :region1DepthName)
        )
        AND (:lastPopupId IS NULL OR p.id > :lastPopupId)
        ORDER BY p.startDate ASC, p.id ASC
    """)
    List<PopupEntity> findFilteredPopups(
            @Param("popupId") Long popupId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("types") List<String> types,
            @Param("categories") List<String> categories,
            @Param("region1DepthName") String region1DepthName,
            @Param("lastPopupId") Long lastPopupId,
            Pageable pageable
    );

    /**
     * 키워드 검색을 위한 팝업 조회 메서드.
     * 키워드가 팝업 제목에 포함되는 팝업을 검색한다.
     */
    @Query("""
                SELECT p FROM PopupEntity p
                WHERE (:popupId IS NULL OR p.id = :popupId)
                AND (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%'))
                AND (:lastPopupId IS NULL OR p.id > :lastPopupId)
                ORDER BY p.startDate ASC, p.id ASC
            """)
    List<PopupEntity> findByKeyword(
            @Param("popupId") Long popupId,
            @Param("keyword") String keyword,
            @Param("lastPopupId") Long lastPopupId,
            Pageable pageable
    );
} 