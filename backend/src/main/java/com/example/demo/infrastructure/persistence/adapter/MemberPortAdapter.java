package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.infrastructure.persistence.mapper.MemberEntityMapper;
import com.example.demo.infrastructure.persistence.repository.MemberJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberPortAdapter implements MemberPort {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberEntityMapper memberEntityMapper;

    public MemberPortAdapter(MemberJpaRepository memberJpaRepository, MemberEntityMapper memberEntityMapper) {
        this.memberJpaRepository = memberJpaRepository;
        this.memberEntityMapper = memberEntityMapper;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id)
                .map(memberEntityMapper::toDomain);
    }

    @Override
    public Member save(Member member) {
        var entity = memberEntityMapper.toEntity(member);
        var savedEntity = memberJpaRepository.save(entity);
        return memberEntityMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        memberJpaRepository.deleteById(id);
    }
} 