package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.CursorResult;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.notification.*;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingDomainEvent;
import com.example.demo.domain.model.waiting.WaitingEventType;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.domain.port.NotificationPort;
import com.example.demo.domain.port.WaitingPort;
import com.example.demo.infrastructure.persistence.entity.NotificationEntity;
import com.example.demo.infrastructure.persistence.mapper.NotificationEntityMapper;
import com.example.demo.infrastructure.persistence.mapper.NotificationEntityMapper.DomainSpecificMapper;
import com.example.demo.infrastructure.persistence.mapper.NotificationEntityMapper.SourceEntityKey;
import com.example.demo.infrastructure.persistence.repository.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationPortAdapter implements NotificationPort {

    private final NotificationJpaRepository repository;
    private final NotificationEntityMapper mapper;
    private final MemberPort memberPort;
    private final WaitingPort waitingPort;

    // 도메인별 매퍼 (지연 초기화)
    private DomainSpecificMapper<Waiting> waitingMapper;

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        NotificationEntity savedEntity = repository.save(entity);
        return getWaitingMapper().toDomain(savedEntity);
    }

    @Override
    public CursorResult<Notification> findAllBy(NotificationQuery query) {
        List<NotificationEntity> entities = executeQuery(query);

        boolean hasNext = false;
        if (entities.size() > query.getPageSize()) {
            entities.removeLast();
            hasNext = true;
        }

        List<Notification> notifications = entities.stream()
                .map(this::entityToDomain)
                .collect(Collectors.toList());

        return new CursorResult<>(notifications, hasNext);
    }

    /**
     * 쿼리 조건에 따라 적절한 Repository 메서드 호출
     */
    private List<NotificationEntity> executeQuery(NotificationQuery query) {
        Long memberId = query.getMemberId();
        ReadStatus status = query.getReadStatus();
        Long lastId = query.getLastNotificationId();
        NotificationSortOrder sortOrder = query.getSortOrder();
        int limit = query.getPageSize() + 1; // hasNext 판단용

        if (lastId == null) {
            // 첫 페이지 조회
            return executeFirstPageQuery(memberId, status, sortOrder, limit);
        } else {
            // 다음 페이지 조회 (커서 기반)
            return executeNextPageQuery(memberId, status, lastId, sortOrder, limit);
        }
    }

    /**
     * 첫 페이지 조회
     */
    private List<NotificationEntity> executeFirstPageQuery(Long memberId, ReadStatus status, NotificationSortOrder sortOrder, int limit) {
        if (status == null) {
            return switch (sortOrder) {
                case UNREAD_FIRST -> repository.findByMemberIdOrderByUnreadFirstThenCreatedAtDesc(memberId, limit);
                case TIME_DESC -> repository.findByMemberIdOrderByCreatedAtDesc(memberId, limit);
            };
        } else {
            return switch (sortOrder) {
                case UNREAD_FIRST -> repository.findByMemberIdAndStatusOrderByUnreadFirstThenCreatedAtDesc(memberId, status, limit);
                case TIME_DESC -> repository.findByMemberIdAndStatusOrderByCreatedAtDesc(memberId, status, limit);
            };
        }
    }

    /**
     * 다음 페이지 조회 (커서 기반)
     */
    private List<NotificationEntity> executeNextPageQuery(Long memberId, ReadStatus status, Long lastId, NotificationSortOrder sortOrder, int limit) {
        if (status == null) {
            return switch (sortOrder) {
                case UNREAD_FIRST -> repository.findByMemberIdAndIdLessThanOrderByUnreadFirstThenCreatedAtDesc(memberId, lastId, limit);
                case TIME_DESC -> repository.findByMemberIdAndIdLessThanOrderByCreatedAtDesc(memberId, lastId, limit);
            };
        } else {
            return switch (sortOrder) {
                case UNREAD_FIRST -> repository.findByMemberIdAndStatusAndIdLessThanOrderByUnreadFirstThenCreatedAtDesc(memberId, status, lastId, limit);
                case TIME_DESC -> repository.findByMemberIdAndStatusAndIdLessThanOrderByCreatedAtDesc(memberId, status, lastId, limit);
            };
        }
    }

    /**
     * 엔티티를 도메인으로 변환 (현재는 Waiting만 지원)
     */
    private Notification entityToDomain(NotificationEntity entity) {
        return switch (entity.getSourceDomain()) {
            case "Waiting" -> getWaitingMapper().toDomain(entity);
            default -> throw new IllegalArgumentException(
                    "지원하지 않는 소스 도메인입니다: " + entity.getSourceDomain()
            );
        };
    }

    /**
     * Waiting 도메인용 매퍼 (지연 초기화)
     */
    private DomainSpecificMapper<Waiting> getWaitingMapper() {
        if (waitingMapper == null) {
            waitingMapper = NotificationEntityMapper
                    .forDomain(Waiting.class)
                    .memberLoader(this::loadMember)
                    .sourceEntityLoader(this::loadWaitingEntity)
                    .domainEventFactory(this::createWaitingEvent)
                    .build();
        }
        return waitingMapper;
    }

    /**
     * 회원 정보 로드
     */
    private Member loadMember(Long memberId) {
        return memberPort.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + memberId));
    }

    /**
     * Waiting 엔티티 로드
     * TODO: WaitingPort에 findById 메서드 추가되면 개선 필요
     */
    private Waiting loadWaitingEntity(SourceEntityKey key) {
        if (!"Waiting".equals(key.sourceDomain())) {
            throw new IllegalArgumentException("Waiting 도메인이 아닙니다: " + key.sourceDomain());
        }

        // 임시 구현: 모든 대기 정보를 조회해서 필터링
        // 실제로는 WaitingPort에 findById 메서드가 필요함
        WaitingQuery query = WaitingQuery.firstPage(null, 1000); // 충분히 큰 사이즈로 조회
        return waitingPort.findByQuery(query)
                .stream()
                .filter(waiting -> waiting.id().equals(key.sourceId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대기 정보입니다: " + key.sourceId()));
    }

    /**
     * Waiting 도메인 이벤트 생성
     */
    private DomainEvent<Waiting> createWaitingEvent(NotificationEntityMapper.SourceEventContext<Waiting> context) {
        WaitingEventType eventType = WaitingEventType.valueOf(context.eventType());
        return new WaitingDomainEvent(context.source(), eventType);
    }
} 