package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.ScheduledNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledNotificationJpaRepository extends JpaRepository<ScheduledNotificationEntity, Long> {
}
