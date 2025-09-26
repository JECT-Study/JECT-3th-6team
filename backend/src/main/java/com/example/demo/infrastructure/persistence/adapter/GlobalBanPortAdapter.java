package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.ban.GlobalBan;
import com.example.demo.domain.port.GlobalBanPort;
import com.example.demo.infrastructure.persistence.entity.GlobalBanEntity;
import com.example.demo.infrastructure.persistence.mapper.GlobalBanEntityMapper;
import com.example.demo.infrastructure.persistence.repository.GlobalBanJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 글로벌 밴 포트 어댑터.
 * 글로벌 밴 포트의 구현체이다.
 */
@Component
@RequiredArgsConstructor
public class GlobalBanPortAdapter implements GlobalBanPort {

    private final GlobalBanJpaRepository globalBanJpaRepository;
    private final GlobalBanEntityMapper globalBanEntityMapper;

    @Override
    public GlobalBan save(GlobalBan globalBan) {
        GlobalBanEntity entity = globalBanEntityMapper.toEntity(globalBan);
        GlobalBanEntity savedEntity = globalBanJpaRepository.save(entity);
        return globalBanEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<GlobalBan> findActiveBanByMemberId(Long memberId) {
        return globalBanJpaRepository.findActiveBanByMemberId(memberId, LocalDate.now())
                .map(globalBanEntityMapper::toDomain);
    }

    @Override
    public List<GlobalBan> findExpiredBans() {
        return globalBanJpaRepository.findExpiredBans(LocalDate.now())
                .stream()
                .map(globalBanEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(List<GlobalBan> globalBans) {
        List<GlobalBanEntity> entities = globalBans.stream()
                .map(globalBanEntityMapper::toEntity)
                .toList();
        globalBanJpaRepository.deleteAll(entities);
    }

    @Override
    public List<GlobalBan> findByMemberId(Long memberId) {
        return globalBanJpaRepository.findByMemberId(memberId)
                .stream()
                .map(globalBanEntityMapper::toDomain)
                .toList();
    }
}
