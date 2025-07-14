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
} 