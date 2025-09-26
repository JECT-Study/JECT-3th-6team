package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.ban.GlobalBan;
import com.example.demo.infrastructure.persistence.entity.GlobalBanEntity;
import org.springframework.stereotype.Component;

/**
 * GlobalBan 도메인 모델과 GlobalBanEntity 간의 변환을 담당하는 Mapper.
 */
@Component
public class GlobalBanEntityMapper {

    /**
     * GlobalBanEntity를 GlobalBan 도메인 모델로 변환한다.
     *
     * @param entity GlobalBanEntity
     * @return GlobalBan 도메인 모델
     */
    public GlobalBan toDomain(GlobalBanEntity entity) {
        return new GlobalBan(
                entity.getId(),
                entity.getMemberId(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }

    /**
     * GlobalBan 도메인 모델을 GlobalBanEntity로 변환한다.
     *
     * @param globalBan GlobalBan 도메인 모델
     * @return GlobalBanEntity
     */
    public GlobalBanEntity toEntity(GlobalBan globalBan) {
        return GlobalBanEntity.builder()
                .id(globalBan.id())
                .memberId(globalBan.memberId())
                .startDate(globalBan.startDate())
                .endDate(globalBan.endDate())
                .build();
    }
}
