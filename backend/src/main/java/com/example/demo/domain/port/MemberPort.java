package com.example.demo.domain.port;

import com.example.demo.domain.model.Member;

import java.util.Optional;

/**
 * 회원 저장소 포트 인터페이스.
 * 회원 정보의 조회를 담당한다.
 */
public interface MemberPort {

    /**
     * ID로 회원 정보를 조회한다.
     *
     * @param id 회원 ID
     * @return 회원 정보 (없으면 empty)
     */
    Optional<Member> findById(Long id);

    /**
     * 회원 정보를 저장(생성 또는 수정)한다.
     *
     * @param member 회원 정보
     * @return 저장된 회원 정보
     */
    Member save(Member member);

    /**
     * 회원 정보를 삭제한다.
     *
     * @param id 회원 ID
     */
    void deleteById(Long id);
} 