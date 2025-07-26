package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.OAuthAccount;
import com.example.demo.infrastructure.persistence.entity.OAuthAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class OAuthAccountEntityMapper {

    public OAuthAccount toDomain(OAuthAccountEntity entity) {
        return new OAuthAccount(
            entity.getId(),
            entity.getProvider(),
            entity.getProviderId(),
            entity.getMemberId()
        );
    }

    public OAuthAccountEntity toEntity(OAuthAccount domain) {
        return OAuthAccountEntity.builder()
            .id(domain.id())
            .provider(domain.provider())
            .providerId(domain.providerId())
            .memberId(domain.memberId())
            .build();
    }
} 