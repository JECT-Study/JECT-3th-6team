package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.WaitingPort;
import com.example.demo.infrastructure.persistence.entity.WaitingEntity;
import com.example.demo.infrastructure.persistence.mapper.WaitingEntityMapper;
import com.example.demo.infrastructure.persistence.repository.WaitingJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingPortAdapter implements WaitingPort {

    private final WaitingJpaRepository waitingJpaRepository;
    private final WaitingEntityMapper waitingEntityMapper;
    private final PopupPortAdapter popupPortAdapter; // 아키텍처 관점에선 다른 어뎁터를 참조하는게 별로 좋지 않지만, 중복 구현을 방지하려면 어쩔 수 없음
    private final MemberPortAdapter memberPortAdapter;

    @Override
    public Waiting save(Waiting waiting) {
        WaitingEntity entity = waitingEntityMapper.toEntity(waiting);
        WaitingEntity savedEntity = waitingJpaRepository.save(entity);

        // 저장된 엔티티를 도메인 모델로 변환
        // 필요한 정보들은 이미 waiting 객체에 있으므로 그대로 사용
        return waitingEntityMapper.toDomain(savedEntity, waiting.popup(), waiting.member());
    }

    @Override
    public List<Waiting> findByQuery(WaitingQuery query) {
        // TODO 다른 기술을 활용한 동적 쿼리 작성 필요
        List<WaitingEntity> waitingEntities = switch (query.sortOrder()) {
            case RESERVED_FIRST_THEN_DATE_DESC ->
                    waitingJpaRepository.findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(
                            query.memberId(), WaitingStatus.WAITING, PageRequest.of(0, query.size()));
        };

        // TODO 성능 고려 해야 함.
        return waitingEntities.parallelStream()
                .map(entity -> {
                    return waitingEntityMapper.toDomain(entity, popupPortAdapter.findById(entity.getPopupId()).orElse(null), new Member(1L, "mock", "mock@mock.com"));
                })
                .toList();
    }

    @Override
    public Integer getNextWaitingNumber(Long popupId) {
        return waitingJpaRepository.findMaxWaitingNumberByPopupId(popupId).orElse(0) + 1;
    }

    @Override
    public Optional<Waiting> findByMemberIdAndPopupId(Long memberId, Long popupId, Popup popup) {
        return waitingJpaRepository.findByMemberIdAndPopupId(memberId, popupId)
            .flatMap(entity -> {
                var member = memberPortAdapter.findById(memberId).orElse(null);
                if (member == null) return Optional.empty();
                return Optional.of(waitingEntityMapper.toDomain(entity, popup, member));
            });
    }
} 