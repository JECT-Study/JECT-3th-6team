package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
} 