package com.example.demo.domain.port;

import com.example.demo.domain.model.ban.Ban;
import com.example.demo.domain.model.ban.BanQuery;

import java.util.List;

/**
 * 제재 정보에 대한 포트 인터페이스.
 */
public interface BanPort {

    /**
     * 제재 정보를 저장한다.
     *
     * @param ban 저장할 제재 정보
     * @return 저장된 제재 정보
     */
    Ban save(Ban ban);

    /**
     * 조건에 맞는 제재 목록을 조회한다.
     *
     * @param query 조회 조건
     * @return 조회된 제재 목록
     */
    List<Ban> findByQuery(BanQuery query);
}