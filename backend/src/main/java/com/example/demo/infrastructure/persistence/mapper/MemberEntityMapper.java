package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.Member;
import com.example.demo.infrastructure.persistence.entity.MemberEntity;
import org.springframework.stereotype.Component;

/**
 * Member 도메인 모델과 MemberEntity 간의 변환을 담당하는 Mapper.
 */
@Component
public class MemberEntityMapper {

    /**
     * MemberEntity를 Member 도메인 모델로 변환한다.
     *
     * @param entity MemberEntity
     * @return Member 도메인 모델
     */
    public Member toDomain(MemberEntity entity) {
        return new Member(
                entity.getId(),
                entity.getName(),
                entity.getEmail()
        );
    }
} 