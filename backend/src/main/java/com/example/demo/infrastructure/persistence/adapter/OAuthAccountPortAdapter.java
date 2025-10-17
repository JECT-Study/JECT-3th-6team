package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.OAuthAccount;
import com.example.demo.domain.model.OAuthProvider;
import com.example.demo.domain.port.OAuthAccountPort;
import com.example.demo.infrastructure.persistence.mapper.OAuthAccountEntityMapper;
import com.example.demo.infrastructure.persistence.repository.OAuthAccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OAuthAccountPortAdapter implements OAuthAccountPort {

    private final OAuthAccountJpaRepository oAuthAccountJpaRepository;
    private final OAuthAccountEntityMapper oAuthAccountEntityMapper;

    @Override
    public Optional<OAuthAccount> findByProviderAndProviderId(OAuthProvider provider, String providerId) {
        return oAuthAccountJpaRepository.findByProviderAndProviderId(provider, providerId)
                .map(oAuthAccountEntityMapper::toDomain);
    }

    @Override
    public OAuthAccount save(OAuthAccount oAuthAccount) {
        var entity = oAuthAccountEntityMapper.toEntity(oAuthAccount);
        var savedEntity = oAuthAccountJpaRepository.save(entity);
        return oAuthAccountEntityMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        oAuthAccountJpaRepository.deleteByMemberId(memberId);
    }
} 