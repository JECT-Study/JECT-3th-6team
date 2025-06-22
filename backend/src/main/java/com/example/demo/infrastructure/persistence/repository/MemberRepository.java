package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

}
