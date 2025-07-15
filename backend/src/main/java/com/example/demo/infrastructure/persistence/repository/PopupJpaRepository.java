package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("""
        SELECT p FROM PopupEntity p
        WHERE (:popupId IS NULL OR p.id = :popupId)
        AND ((:startDate IS NULL OR :endDate IS NULL) OR (p.endDate >= :startDate AND p.startDate <= :endDate))
        AND (:types IS NULL OR p.type IN :types)
        AND EXISTS (
            SELECT 1 FROM PopupCategoryEntity c
            WHERE c.popupId = p.id AND (:categories IS NULL OR c.name IN :categories)
        )
        AND EXISTS (
            SELECT 1 FROM PopupLocationEntity l
            WHERE l.id = p.popupLocationId AND (:region1DepthName IS NULL OR l.region1DepthName = :region1DepthName)
        )
        ORDER BY p.startDate ASC
    """)
    List<PopupEntity> findFilteredPopups(
        @Param("popupId") Long popupId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("types") List<String> types,
        @Param("categories") List<String> categories,
        @Param("region1DepthName") String region1DepthName,
        Pageable pageable
    );
} 