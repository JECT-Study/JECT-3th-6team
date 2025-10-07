package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.WaitingPort;
import com.example.demo.infrastructure.persistence.entity.QWaitingEntity;
import com.example.demo.infrastructure.persistence.entity.WaitingEntity;
import com.example.demo.infrastructure.persistence.mapper.WaitingEntityMapper;
import com.example.demo.infrastructure.persistence.repository.WaitingJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    private final JPAQueryFactory jpaQueryFactory;

    private static final QWaitingEntity waitingEntity = QWaitingEntity.waitingEntity;

    @Override
    public Waiting save(Waiting waiting) {
        WaitingEntity entity = waitingEntityMapper.toEntity(waiting);
        WaitingEntity savedEntity = waitingJpaRepository.save(entity);

        // 저장된 엔티티를 도메인 모델로 변환
        // 필요한 정보들은 이미 waiting 객체에 있으므로 그대로 사용
        return waitingEntityMapper.toDomain(savedEntity, waiting.popup(), waiting.member());
    }

    @Override
    public List<Waiting> saveAll(List<Waiting> waitings) { // TODO 성능 고려 필요
        return waitings.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public List<Waiting> findByQuery(WaitingQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        List<WaitingEntity> waitingEntities;

        switch (query) {
            case WaitingQuery.ForWaitingId q -> {
                builder.and(waitingEntity.id.eq(q.getWaitingId()));
                waitingEntities = jpaQueryFactory.selectFrom(waitingEntity)
                        .where(builder)
                        .fetch();
            }
            case WaitingQuery.ForVisitHistory q -> {
                // 복잡한 정렬 로직이 필요하므로 기존 JPA 메서드 사용
                waitingEntities = waitingJpaRepository.findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(
                        q.getMemberId(), WaitingStatus.WAITING, PageRequest.of(0, q.getSize()));
            }
            case WaitingQuery.ForPopup q -> {
                builder.and(waitingEntity.popupId.eq(q.getPopupId()));
                if (q.getStatus() != null) {
                    builder.and(waitingEntity.status.eq(q.getStatus()));
                }
                waitingEntities = jpaQueryFactory.selectFrom(waitingEntity)
                        .where(builder)
                        .orderBy(waitingEntity.waitingNumber.asc())
                        .fetch();
            }
            case WaitingQuery.ForDuplicateCheck q -> {
                builder.and(waitingEntity.memberId.eq(q.getMemberId()))
                        .and(waitingEntity.popupId.eq(q.getPopupId()));
                if (q.getDate() != null) {
                    builder.and(waitingEntity.createdAt.between(
                            q.getDate().atStartOfDay(),
                            q.getDate().plusDays(1).atStartOfDay()
                    ));
                }
                waitingEntities = jpaQueryFactory.selectFrom(waitingEntity)
                        .where(builder)
                        .fetch();
            }
            case WaitingQuery.ForMemberAndPopupOnDate q -> {
                builder.and(waitingEntity.memberId.eq(q.getMemberId()))
                        .and(waitingEntity.popupId.eq(q.getPopupId()));
                if (q.getDate() != null) {
                    builder.and(waitingEntity.createdAt.between(
                            q.getDate().atStartOfDay(),
                            q.getDate().plusDays(1).atStartOfDay()
                    ));
                }
                waitingEntities = jpaQueryFactory.selectFrom(waitingEntity)
                        .where(builder)
                        .fetch();
            }
            case WaitingQuery.ForStatus q -> {
                builder.and(waitingEntity.status.eq(q.getStatus()));
                waitingEntities = jpaQueryFactory.selectFrom(waitingEntity)
                        .where(builder)
                        .fetch();
            }
            case WaitingQuery.ForMemberAndPopupWithStatus q -> {
                builder.and(waitingEntity.memberId.eq(q.getMemberId()))
                        .and(waitingEntity.popupId.eq(q.getPopupId()))
                        .and(waitingEntity.status.eq(q.getStatus()));
                waitingEntities = jpaQueryFactory.selectFrom(waitingEntity)
                        .where(builder)
                        .fetch();
            }
            case null, default -> throw new BusinessException(ErrorType.FEATURE_NOT_IMPLEMENTED);
        }

        return waitingEntities.stream()
                .map(this::mapEntityToDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkDuplicate(WaitingQuery query) {
        return waitingJpaRepository.existsByMemberIdAndPopupId(query.getMemberId(), query.getPopupId());
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