package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.WaitingEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * WaitingEntity를 위한 Spring Data JPA Repository.
 */
@Repository
public interface WaitingJpaRepository extends JpaRepository<WaitingEntity, Long> {

    /**
     * 회원 ID로 대기 목록을 조회한다.
     * 생성일 기준 내림차순으로 정렬한다.
     * 
     * @param memberId 회원 ID
     * @param pageable 페이징 정보
     * @return 대기 엔티티 목록
     */
    List<WaitingEntity> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
    
    /**
     * 회원 ID로 대기 목록을 조회한다.
     * RESERVED 상태가 먼저, 그 다음 생성일 기준 내림차순으로 정렬한다.
     * 
     * @param memberId 회원 ID
     * @param pageable 페이징 정보
     * @return 대기 엔티티 목록
     */
    @Query("SELECT w FROM WaitingEntity w WHERE w.memberId = :memberId ORDER BY " +
           "CASE WHEN w.status = 'RESERVED' THEN 0 ELSE 1 END, " +
           "w.createdAt DESC")
    List<WaitingEntity> findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(
            @Param("memberId") Long memberId, Pageable pageable);
    
    /**
     * 팝업 ID로 최대 대기 번호를 조회한다.
     * 
     * @param popupId 팝업 ID
     * @return 최대 대기 번호 (없으면 0)
     */
    @Query("SELECT MAX(w.waitingNumber) FROM WaitingEntity w WHERE w.popupId = :popupId")
    Optional<Integer> findMaxWaitingNumberByPopupId(@Param("popupId") Long popupId);
} 
