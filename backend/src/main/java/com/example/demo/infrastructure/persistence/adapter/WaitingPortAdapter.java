package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.WaitingPort;
import com.example.demo.infrastructure.persistence.entity.WaitingEntity;
import com.example.demo.infrastructure.persistence.mapper.WaitingEntityMapper;
import com.example.demo.infrastructure.persistence.repository.WaitingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (query.waitingId() != null) {
            return waitingJpaRepository.findById(query.waitingId())
                    .stream()
                    .map(this::mapEntityToDomain)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        // 팝업별 대기 조회
        if (query.popupId() != null) {
            List<WaitingEntity> entities = waitingJpaRepository.findByPopupIdAndStatusOrderByWaitingNumberAsc(
                    query.popupId(), query.status());
            return entities.stream()
                    .map(this::mapEntityToDomain)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        List<WaitingEntity> waitingEntities = switch (query.sortOrder()) {
            case RESERVED_FIRST_THEN_DATE_DESC ->
                    waitingJpaRepository.findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(
                            query.memberId(), WaitingStatus.WAITING, PageRequest.of(0, query.size()));
        };
        // TODO 성능 고려 해야 함.
        return waitingEntities.stream()
                .map(this::mapEntityToDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Waiting mapEntityToDomain(WaitingEntity entity) {
        var popup = popupPortAdapter.findById(entity.getPopupId()).orElse(null);
        var member = memberPortAdapter.findById(entity.getMemberId()).orElse(null);
        if (popup == null || member == null) {
            return null;
        }
        return waitingEntityMapper.toDomain(entity, popup, member);
    }

    @Override
    public Integer getNextWaitingNumber(Long popupId) {
        List<WaitingEntity> allWaitng = waitingJpaRepository.findByPopupIdAndStatusOrderByWaitingNumberAsc(popupId, WaitingStatus.WAITING);

        if (allWaitng.isEmpty()) {
            return 0; // 아무도 대기하지 않는 경우 0 반환
        }
        
        return allWaitng.stream()
                .mapToInt(WaitingEntity::getWaitingNumber)
                .max()
                .orElse(0) + 1; // 최대 대기 번호 + 1 반환
    }

    @Override
    public Optional<Waiting> findByMemberIdAndPopupId(Long memberId, Long popupId) {
        return waitingJpaRepository.findByMemberIdAndPopupId(memberId, popupId)
                .flatMap(entity -> {
                    var popup = popupPortAdapter.findById(entity.getPopupId()).orElse(null);
                    var member = memberPortAdapter.findById(entity.getMemberId()).orElse(null);
                    if (popup == null || member == null) return Optional.empty();
                    return Optional.of(waitingEntityMapper.toDomain(entity, popup, member));
                });
    }

} 