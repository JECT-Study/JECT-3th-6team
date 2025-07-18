package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.model.popup.PopupMapQuery;
import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PopupEntity를 위한 Spring Data JPA Repository.
 */
@Repository
public interface PopupJpaRepository extends JpaRepository<PopupEntity, Long> {

    @Query("""
            SELECT DISTINCT p FROM PopupEntity p \
            JOIN PopupLocationEntity pl ON p.popupLocationId = pl.id \
            LEFT JOIN PopupCategoryEntity pc ON p.id = pc.popupId \
            WHERE pl.latitude BETWEEN :#{#query.minLatitude} AND :#{#query.maxLatitude} \
            AND pl.longitude BETWEEN :#{#query.minLongitude} AND :#{#query.maxLongitude} \
            AND (:#{#query.type} IS NULL OR CAST(p.type AS string) = CAST(:#{#query.type} AS string)) \
            AND (
                ((:#{#query.dateRange.startDate()} IS NULL) OR (:#{#query.dateRange.endDate()} IS NULL))
                OR
                (p.startDate <= :#{#query.dateRange.endDate} AND p.endDate >= :#{#query.dateRange.startDate})
                ) \
            AND (:#{#query.categories} IS NULL OR pc.name IN (:#{#query.categories}))
            """
    )
    List<PopupEntity> findByMapQuery(@Param("query") PopupMapQuery query);
} 