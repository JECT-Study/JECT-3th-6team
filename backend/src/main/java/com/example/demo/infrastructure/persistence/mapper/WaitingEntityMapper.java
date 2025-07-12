package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.infrastructure.persistence.entity.WaitingEntity;
import org.springframework.stereotype.Component;

/**
 * Waiting 도메인 모델과 WaitingEntity 간의 변환을 담당하는 Mapper.
 */
@Component
public class WaitingEntityMapper {

    /**
     * WaitingEntity를 Waiting 도메인 모델로 변환한다.
     *
     * @param entity WaitingEntity
     * @param popup  팝업 상세 정보
     * @param member 회원 정보
     * @return Waiting 도메인 모델
     */
    public Waiting toDomain(WaitingEntity entity, Popup popup, Member member) {
        return new Waiting(
                entity.getId(),
                popup,
                entity.getWaitingPersonName(),
                member,
                entity.getContactEmail(),
                entity.getPeopleCount(),
                entity.getWaitingNumber(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    /**
     * Waiting 도메인 모델을 WaitingEntity로 변환한다.
     *
     * @param waiting Waiting 도메인 모델
     * @return WaitingEntity
     */
    public WaitingEntity toEntity(Waiting waiting) {
        return WaitingEntity.builder()
                .popupId(waiting.popup().getId())
                .memberId(waiting.member().id())
                .waitingPersonName(waiting.waitingPersonName())
                .contactEmail(waiting.contactEmail())
                .peopleCount(waiting.peopleCount())
                .waitingNumber(waiting.waitingNumber())
                .status(waiting.status())
                .build();
    }

    public Waiting toDomain(WaitingEntity entity) {
        return new Waiting(
            entity.getId(),
            null, // popup 생략
            entity.getWaitingPersonName(),
            null, // member 생략
            entity.getContactEmail(),
            entity.getPeopleCount(),
            entity.getWaitingNumber(),
            entity.getStatus(),
            entity.getCreatedAt()
        );
    }
} 