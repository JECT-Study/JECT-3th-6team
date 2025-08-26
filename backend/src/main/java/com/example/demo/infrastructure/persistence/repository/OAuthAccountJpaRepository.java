package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.model.OAuthProvider;
import com.example.demo.infrastructure.persistence.entity.OAuthAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthAccountJpaRepository extends JpaRepository<OAuthAccountEntity, Long> {

    Optional<OAuthAccountEntity> findByProviderAndProviderId(OAuthProvider provider, String providerId);
} 