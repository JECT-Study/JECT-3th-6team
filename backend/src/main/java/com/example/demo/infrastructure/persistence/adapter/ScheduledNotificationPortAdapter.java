package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.notification.*;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingDomainEvent;
import com.example.demo.domain.model.waiting.WaitingEventType;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.domain.port.ScheduledNotificationPort;
import com.example.demo.domain.port.WaitingPort;
import com.example.demo.infrastructure.persistence.entity.NotificationEntity;
import com.example.demo.infrastructure.persistence.entity.ScheduledNotificationEntity;
import com.example.demo.infrastructure.persistence.mapper.NotificationEntityMapper;
import com.example.demo.infrastructure.persistence.mapper.NotificationEntityMapper.DomainSpecificMapper;
import com.example.demo.infrastructure.persistence.mapper.ScheduledNotificationEntityMapper;
import com.example.demo.infrastructure.persistence.repository.ScheduledNotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduledNotificationPortAdapter implements ScheduledNotificationPort {

    private final ScheduledNotificationJpaRepository repository;
    private final ScheduledNotificationEntityMapper mapper;
    private final MemberPort memberPort;
    private final WaitingPort waitingPort;

    private DomainSpecificMapper<Waiting> waitingMapper;

    @Override
    public ScheduledNotification save(ScheduledNotification scheduledNotification) {
        Notification reservatedNotification = scheduledNotification.getReservatedNotification();
        ScheduledNotificationTrigger notificationTrigger = scheduledNotification.getScheduledNotificationTrigger();

        ScheduledNotificationEntity entity = mapper.toEntity(scheduledNotification);
        ScheduledNotificationEntity saved = repository.save(entity);

        return new ScheduledNotification(
                saved.getId(),
                reservatedNotification,
                notificationTrigger
        );
    }

    @Override
    public List<ScheduledNotification> findAllByQuery(ScheduledNotificationQuery query) {
        List<ScheduledNotificationEntity> allScheduledNotificationEntities = repository.findAll();

        return allScheduledNotificationEntities.stream()
                .map(this::entityToDomain)
                .toList();
    }

    @Override
    public void delete(List<ScheduledNotification> scheduledNotifications) {
        repository.deleteAllById(scheduledNotifications.stream().map(ScheduledNotification::getId).toList());
    }

    private ScheduledNotification entityToDomain(ScheduledNotificationEntity entity) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .sourceDomain(entity.getSourceDomain())
                .sourceId(entity.getSourceId())
                .eventType(entity.getEventType())
                .content(entity.getContent())
                .status(entity.getStatus())
                .readAt(entity.getReadAt())
                .build();

        var notification = switch (entity.getSourceDomain()) {
            case "Waiting" -> getWaitingMapper().toDomain(notificationEntity);
            default -> throw new IllegalArgumentException(
                    "지원하지 않는 소스 도메인입니다: " + entity.getSourceDomain()
            );
        };

        return new ScheduledNotification(
                entity.getId(),
                notification,
                entity.getTrigger()
        );
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
    private Waiting loadWaitingEntity(NotificationEntityMapper.SourceEntityKey key) {
        if (!"Waiting".equals(key.sourceDomain())) {
            throw new IllegalArgumentException("Waiting 도메인이 아닙니다: " + key.sourceDomain());
        }

        // 임시 구현: 모든 대기 정보를 조회해서 필터링
        // 실제로는 WaitingPort에 findById 메서드가 필요함
        WaitingQuery query = WaitingQuery.firstPage(1L, 1000); // 충분히 큰 사이즈로 조회
        List<Waiting> byQuery = waitingPort.findByQuery(query);
        return byQuery
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
