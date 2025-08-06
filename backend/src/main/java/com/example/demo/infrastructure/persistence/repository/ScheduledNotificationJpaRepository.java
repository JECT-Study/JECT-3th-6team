package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.ScheduledNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ScheduledNotificationJpaRepository extends JpaRepository<ScheduledNotificationEntity, Long> {

    /**
     * 입장 알림 발송 시간 업데이트
     */
    @Modifying
    @Query("UPDATE ScheduledNotificationEntity s SET s.enterNotificationSentAt = :sentAt WHERE s.id = :id")
    void updateEnterNotificationSentAt(@Param("id") Long id, @Param("sentAt") LocalDateTime sentAt);

    /**
     * 웨이팅 ID로 해당하는 모든 스케줄된 알림의 실제 입장 시간 업데이트
     */
    @Modifying
    @Query("UPDATE ScheduledNotificationEntity s SET s.actualEnterTime = :actualEnterTime WHERE s.sourceId = :waitingId")
    void updateActualEnterTimeByWaitingId(@Param("waitingId") Long waitingId, @Param("actualEnterTime") LocalDateTime actualEnterTime);
}
