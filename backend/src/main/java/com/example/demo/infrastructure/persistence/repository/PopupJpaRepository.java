package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PopupEntity를 위한 Spring Data JPA Repository.
 */
@Repository
public interface PopupJpaRepository extends JpaRepository<PopupEntity, Long> {

    /**
     * ID로 팝업을 조회한다.
     *
     * @param id 팝업 ID
     * @return 팝업 엔티티
     */
    Optional<PopupEntity> findById(Long id);

    @Query("SELECT p FROM PopupEntity p " +
            "LEFT JOIN FETCH p.location " +
            "LEFT JOIN p.categories pc " +
            "WHERE p.location.latitude BETWEEN :minLatitude AND :maxLatitude " +
            "AND p.location.longitude BETWEEN :minLongitude AND :maxLongitude " +
            "AND (:#{#query.type} IS NULL OR p.popupInfo.type = :#{#query.type}) " +
            "AND (:#{#query.dateRange} IS NULL OR (p.schedule.startDate <= :#{#query.dateRange.endDate} AND p.schedule.endDate >= :#{#query.dateRange.startDate})) " +
            "AND (:#{#query.categories} IS NULL OR pc.category.name IN (:#{#query.categories}))"
    )
    List<PopupEntity> findPopupsByMapQuery(@Param("query") com.example.demo.domain.model.popup.PopupMapQuery query,
                                           @Param("minLatitude") BigDecimal minLatitude,
                                           @Param("maxLatitude") BigDecimal maxLatitude,
                                           @Param("minLongitude") BigDecimal minLongitude,
                                           @Param("maxLongitude") BigDecimal maxLongitude
                                           );
} 