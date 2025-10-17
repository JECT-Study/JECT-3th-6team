package com.example.demo.domain.port;

import com.example.demo.domain.model.OAuthAccount;
import com.example.demo.domain.model.OAuthProvider;

import java.util.Optional;

public interface OAuthAccountPort {

    Optional<OAuthAccount> findByProviderAndProviderId(OAuthProvider provider, String providerId);

    OAuthAccount save(OAuthAccount oAuthAccount);

    void deleteByMemberId(Long memberId);
} 