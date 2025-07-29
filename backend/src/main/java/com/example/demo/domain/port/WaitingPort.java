package com.example.demo.domain.port;

import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import java.util.List;
import java.util.Optional;
import com.example.demo.domain.model.CursorResult;

/**
 * 대기 저장소 포트 인터페이스.
 * 대기 정보의 영속성을 담당한다.
 */
public interface WaitingPort {

    /**
     * 대기 정보를 저장한다.
     *
     * @param waiting 저장할 대기 정보
     * @return 저장된 대기 정보ㅇ
     */
    Waiting save(Waiting waiting);

    /**
     * 조회 조건에 따라 대기 정보 목록을 조회한다.
     * (waitingId가 있으면 단건 조회로 동작)
     *
     * @param query 조회 조건
     * @return 대기 정보 목록
     */
    List<Waiting> findByQuery(WaitingQuery query);

    /**
     * 팝업의 다음 대기 번호를 조회한다.
     *
     * @param popupId 팝업 ID
     * @return 다음 대기 번호
     */
    Integer getNextWaitingNumber(Long popupId);
    Optional<Waiting> findByMemberIdAndPopupId(Long memberId, Long popupId);
    
} 
