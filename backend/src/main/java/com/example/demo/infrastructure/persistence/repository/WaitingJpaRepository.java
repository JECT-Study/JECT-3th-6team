package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.infrastructure.persistence.entity.WaitingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * WaitingEntity를 위한 Spring Data JPA Repository.
 */
@Repository
public interface WaitingJpaRepository extends JpaRepository<WaitingEntity, Long> {

    /**
     * 회원 ID로 대기 목록을 조회한다.
     * RESERVED 상태가 먼저, 그 다음 생성일 기준 내림차순으로 정렬한다.
     *
     * @param memberId 회원 ID
     * @param pageable 페이징 정보
     * @return 대기 엔티티 목록
     */
    @Query("SELECT w FROM WaitingEntity w WHERE w.memberId = :memberId ORDER BY " +
            "CASE WHEN w.status = :reservedStatus THEN 0 ELSE 1 END, " +
            "w.createdAt DESC")
    List<WaitingEntity> findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(
            @Param("memberId") Long memberId,
            @Param("reservedStatus") WaitingStatus reservedStatus,
            Pageable pageable);

    /**
     * 팝업 ID로 최대 대기 번호를 조회한다.
     *
     * @param popupId 팝업 ID
     * @return 최대 대기 번호 (없으면 0)
     */
    @Query("SELECT MAX(w.waitingNumber) FROM WaitingEntity w WHERE w.popupId = :popupId")
    Optional<Integer> findMaxWaitingNumberByPopupId(@Param("popupId") Long popupId);

    Optional<WaitingEntity> findByMemberIdAndPopupId(Long memberId, Long popupId);

    /**
     * 특정 팝업의 대기중인 모든 대기를 대기번호 순으로 조회한다.
     *
     * @param popupId 팝업 ID
     * @param status 대기 상태
     * @return 대기 엔티티 목록 (대기번호 순으로 정렬)
     */
    List<WaitingEntity> findByPopupIdAndStatusOrderByWaitingNumber(Long popupId, WaitingStatus status);
} 
