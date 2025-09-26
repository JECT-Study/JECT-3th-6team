package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.GlobalBanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 글로벌 밴 JPA 리포지토리.
 */
@Repository
public interface GlobalBanJpaRepository extends JpaRepository<GlobalBanEntity, Long> {

    /**
     * 특정 회원의 현재 유효한 글로벌 밴을 조회한다.
     *
     * @param memberId 회원 ID
     * @param today 오늘 날짜
     * @return 유효한 글로벌 밴 (있으면)
     */
    @Query("SELECT g FROM GlobalBanEntity g WHERE g.memberId = :memberId " +
           "AND g.startDate <= :today AND g.endDate >= :today")
    Optional<GlobalBanEntity> findActiveBanByMemberId(@Param("memberId") Long memberId, @Param("today") LocalDate today);

    /**
     * 만료된 글로벌 밴들을 조회한다.
     *
     * @param today 오늘 날짜
     * @return 만료된 글로벌 밴 목록
     */
    @Query("SELECT g FROM GlobalBanEntity g WHERE g.endDate < :today")
    List<GlobalBanEntity> findExpiredBans(@Param("today") LocalDate today);

    /**
     * 특정 회원의 모든 글로벌 밴을 조회한다.
     *
     * @param memberId 회원 ID
     * @return 글로벌 밴 목록
     */
    List<GlobalBanEntity> findByMemberId(Long memberId);
}
