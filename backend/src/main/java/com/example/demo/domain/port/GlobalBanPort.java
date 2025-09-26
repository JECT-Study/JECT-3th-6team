package com.example.demo.domain.port;

import com.example.demo.domain.model.ban.GlobalBan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 글로벌 밴 포트.
 * 글로벌 밴 도메인과 인프라스트럭처 간의 계약을 정의한다.
 */
public interface GlobalBanPort {

    /**
     * 글로벌 밴을 저장한다.
     *
     * @param globalBan 저장할 글로벌 밴
     * @return 저장된 글로벌 밴
     */
    GlobalBan save(GlobalBan globalBan);

    /**
     * 특정 회원의 현재 유효한 글로벌 밴을 조회한다.
     *
     * @param memberId 회원 ID
     * @return 유효한 글로벌 밴 (있으면)
     */
    Optional<GlobalBan> findActiveBanByMemberId(Long memberId);

    /**
     * 만료된 글로벌 밴들을 조회한다.
     *
     * @return 만료된 글로벌 밴 목록
     */
    List<GlobalBan> findExpiredBans();

    /**
     * 글로벌 밴들을 삭제한다.
     *
     * @param globalBans 삭제할 글로벌 밴 목록
     */
    void delete(List<GlobalBan> globalBans);

    /**
     * 특정 회원의 모든 글로벌 밴을 조회한다.
     *
     * @param memberId 회원 ID
     * @return 글로벌 밴 목록
     */
    List<GlobalBan> findByMemberId(Long memberId);
}
