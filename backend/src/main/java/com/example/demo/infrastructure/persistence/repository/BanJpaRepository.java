package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.BanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanJpaRepository extends JpaRepository<BanEntity, Long> {
}
