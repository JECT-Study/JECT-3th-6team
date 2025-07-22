package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.notification.DomainEvent;
import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.notification.NotificationStatus;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.infrastructure.persistence.entity.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class NotificationEntityMapper {

    /**
     * 특정 도메인에 특화된 매퍼 클래스
     */
    public static class DomainSpecificMapper<T> {
        private final Function<Long, Member> memberLoader;
        private final Function<SourceEntityKey, T> sourceEntityLoader;
        private final Function<SourceEventContext<T>, DomainEvent<T>> domainEventFactory;

        private DomainSpecificMapper(
                Function<Long, Member> memberLoader,
                Function<SourceEntityKey, T> sourceEntityLoader,
                Function<SourceEventContext<T>, DomainEvent<T>> domainEventFactory
        ) {
            this.memberLoader = memberLoader;
            this.sourceEntityLoader = sourceEntityLoader;
            this.domainEventFactory = domainEventFactory;
        }

        /**
         * 엔티티를 도메인 모델로 변환
         */
        public Notification toDomain(NotificationEntity entity) {
            if (entity == null) return null;

            // 회원 정보 로드
            Member member = memberLoader.apply(entity.getMemberId());

            // 소스 엔티티 로드
            SourceEntityKey sourceKey = new SourceEntityKey(entity.getSourceDomain(), entity.getSourceId());
            T sourceEntity = sourceEntityLoader.apply(sourceKey);

            // 도메인 이벤트 생성
            SourceEventContext<T> eventContext = new SourceEventContext<>(sourceEntity, entity.getEventType());
            DomainEvent<T> domainEvent = domainEventFactory.apply(eventContext);

            // 알림 상태 복원
            NotificationStatus status = NotificationStatus.of(entity.getStatus(), entity.getReadAt());

            return Notification.builder()
                    .id(entity.getId())
                    .member(member)
                    .event(domainEvent)
                    .content(entity.getContent())
                    .status(status)
                    .createdAt(entity.getCreatedAt())
                    .build();
        }
    }

    /**
     * 도메인별 매퍼 빌더
     */
    public static class DomainSpecificMapperBuilder<T> {
        private Function<Long, Member> memberLoader;
        private Function<SourceEntityKey, T> sourceEntityLoader;
        private Function<SourceEventContext<T>, DomainEvent<T>> domainEventFactory;

        public DomainSpecificMapperBuilder<T> memberLoader(Function<Long, Member> memberLoader) {
            this.memberLoader = memberLoader;
            return this;
        }

        public DomainSpecificMapperBuilder<T> sourceEntityLoader(Function<SourceEntityKey, T> sourceEntityLoader) {
            this.sourceEntityLoader = sourceEntityLoader;
            return this;
        }

        public DomainSpecificMapperBuilder<T> domainEventFactory(Function<SourceEventContext<T>, DomainEvent<T>> domainEventFactory) {
            this.domainEventFactory = domainEventFactory;
            return this;
        }

        public DomainSpecificMapper<T> build() {
            if (memberLoader == null || sourceEntityLoader == null || domainEventFactory == null) {
                throw new IllegalStateException("모든 필수 함수형 인터페이스가 설정되어야 합니다.");
            }
            return new DomainSpecificMapper<>(memberLoader, sourceEntityLoader, domainEventFactory);
        }
    }

    /**
     * 소스 엔티티 식별을 위한 키 클래스
     */
    public record SourceEntityKey(String sourceDomain, Long sourceId) {
    }

    /**
     * 이벤트 생성을 위한 컨텍스트 클래스
     */
    public record SourceEventContext<T>(T source, String eventType) {
    }

    /**
     * 특정 도메인에 특화된 매퍼 빌더 생성
     */
    public static <T> DomainSpecificMapperBuilder<T> forDomain(Class<T> domainClass) {
        return new DomainSpecificMapperBuilder<>();
    }

    /**
     * 소스 객체에서 ID를 추출하는 헬퍼 메서드
     */
    private Long extractSourceId(Object source) {
        return switch (source) {
            case Waiting waiting -> waiting.id();
            default -> throw new IllegalArgumentException(
                    "지원하지 않는 소스 도메인 타입입니다: " + source.getClass().getSimpleName()
            );
        };
    }

    /**
     * 도메인 모델을 엔티티로 변환 (변환 로직이 공통적이므로 static 메서드로 유지)
     */
    public NotificationEntity toEntity(Notification notification) {
        if (notification == null) return null;

        DomainEvent<?> event = notification.getEvent();
        Object source = event.getSource();

        return NotificationEntity.builder()
                .id(notification.getId())
                .memberId(notification.getMember().id())
                .sourceDomain(source.getClass().getSimpleName())
                .sourceId(extractSourceId(source))
                .eventType(event.getEventType())
                .content(notification.getContent())
                .status(notification.getStatus().getStatus())
                .readAt(notification.getStatus().getReadAt())
                .build();
    }
}