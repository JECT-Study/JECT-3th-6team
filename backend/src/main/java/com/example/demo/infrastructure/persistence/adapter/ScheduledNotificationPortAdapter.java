package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
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
                .id(null)
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
            default -> throw new BusinessException(ErrorType.UNSUPPORTED_NOTIFICATION_TYPE, entity.getSourceDomain());
        };

        return new ScheduledNotification(
                entity.getId(),
                notification,
                entity.getTrigger(),
                entity.getEnterNotificationSentAt(),
                entity.getActualEnterTime()
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
                .orElseThrow(() -> new BusinessException(ErrorType.MEMBER_NOT_FOUND, String.valueOf(memberId)));
    }

    /**
     * Waiting 엔티티 로드
     */
    private Waiting loadWaitingEntity(NotificationEntityMapper.SourceEntityKey key) {
        if (!"Waiting".equals(key.sourceDomain())) {
            throw new BusinessException(ErrorType.INVALID_SOURCE_DOMAIN, key.sourceDomain());
        }

        WaitingQuery query = WaitingQuery.forWaitingId(key.sourceId());
        List<Waiting> byQuery = waitingPort.findByQuery(query);
        return byQuery
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorType.WAITING_NOT_FOUND, String.valueOf(key.sourceId())));
    }

    /**
     * Waiting 도메인 이벤트 생성
     */
    private DomainEvent<Waiting> createWaitingEvent(NotificationEntityMapper.SourceEventContext<Waiting> context) {
        WaitingEventType eventType = WaitingEventType.valueOf(context.eventType());
        return new WaitingDomainEvent(context.source(), eventType);
    }

    @Override
    public void updateEnterNotificationSentAt(Long scheduledNotificationId, LocalDateTime sentAt) {
        scheduledNotificationJpaRepository.updateEnterNotificationSentAt(scheduledNotificationId, sentAt);
    }

    @Override
    public void updateActualEnterTime(Long waitingId, LocalDateTime actualEnterTime) {
        scheduledNotificationJpaRepository.updateActualEnterTimeByWaitingId(waitingId, actualEnterTime);
    }
}
