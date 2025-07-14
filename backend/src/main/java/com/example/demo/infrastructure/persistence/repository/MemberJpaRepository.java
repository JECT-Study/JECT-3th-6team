package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MemberEntity를 위한 Spring Data JPA Repository.
 */
@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    
    /**
     * ID로 회원을 조회한다.
     * 
     * @param id 회원 ID
     * @return 회원 엔티티
     */
    Optional<MemberEntity> findById(Long id);
} 