package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    @PersistenceContext
    private final EntityManager em;

    // 도메인별 매퍼 (지연 초기화)
    private DomainSpecificMapper<Waiting> waitingMapper;

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        NotificationEntity savedEntity = repository.save(entity);
        return getWaitingMapper().toDomain(savedEntity);
    }

    @Override
    public void delete(Notification notification) {
        if (notification.getId() == null) {
            throw new BusinessException(ErrorType.NULL_NOTIFICATION_ID);
        }
        repository.deleteById(notification.getId());
    }

    @Override
    public CursorResult<Notification> findAllBy(NotificationQuery query) {
        if (query.getNotificationId() != null) {
            // 단일 알림 조회
            Notification notification = repository.findById(query.getNotificationId())
                    .map(this::entityToDomain)
                    .orElseThrow(() -> new BusinessException(ErrorType.NOTIFICATION_NOT_FOUND, String.valueOf(query.getNotificationId())));
            return new CursorResult<>(List.of(notification), false);
        }
        List<NotificationEntity> entities = executeQuery(query);

        boolean hasNext = false;
        if (entities.size() > query.getPageSize()) {
            entities.removeLast();
            hasNext = true;
        }

        List<Notification> notifications = entities.stream()
                .map(this::entityToDomain)
                .filter(notification -> notification.getEvent().getSource() != null) // 소스가 없는 알림은 제외
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
                case UNREAD_FIRST ->
                        repository.findByMemberIdAndStatusOrderByUnreadFirstThenCreatedAtDesc(memberId, status, limit);
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
                case UNREAD_FIRST ->
                        repository.findByMemberIdAndIdLessThanOrderByUnreadFirstThenCreatedAtDesc(memberId, lastId, limit);
                case TIME_DESC -> repository.findByMemberIdAndIdLessThanOrderByCreatedAtDesc(memberId, lastId, limit);
            };
        } else {
            return switch (sortOrder) {
                case UNREAD_FIRST ->
                        repository.findByMemberIdAndStatusAndIdLessThanOrderByUnreadFirstThenCreatedAtDesc(memberId, status, lastId, limit);
                case TIME_DESC ->
                        repository.findByMemberIdAndStatusAndIdLessThanOrderByCreatedAtDesc(memberId, status, lastId, limit);
            };
        }
    }

    /**
     * 엔티티를 도메인으로 변환 (현재는 Waiting만 지원)
     */
    private Notification entityToDomain(NotificationEntity entity) {
        return switch (entity.getSourceDomain()) {
            case "Waiting" -> getWaitingMapper().toDomain(entity);
            default -> throw new BusinessException(ErrorType.UNSUPPORTED_NOTIFICATION_TYPE, entity.getSourceDomain());
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
                .orElseThrow(() -> new BusinessException(ErrorType.MEMBER_NOT_FOUND, String.valueOf(memberId)));
    }

    /**
     * Waiting 엔티티 로드
     */
    private Waiting loadWaitingEntity(SourceEntityKey key) {
        if (!"Waiting".equals(key.sourceDomain())) {
            throw new BusinessException(ErrorType.INVALID_SOURCE_DOMAIN, key.sourceDomain());
        }

        WaitingQuery query = WaitingQuery.forWaitingId(key.sourceId());
        List<Waiting> byQuery = waitingPort.findByQuery(query);
        return byQuery
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Waiting 도메인 이벤트 생성
     */
    private DomainEvent<Waiting> createWaitingEvent(NotificationEntityMapper.SourceEventContext<Waiting> context) {
        WaitingEventType eventType = WaitingEventType.valueOf(context.eventType());
        return new WaitingDomainEvent(context.source(), eventType);
    }
} 