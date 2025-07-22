package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.domain.model.notification.ReadStatus;
import com.example.demo.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

    // 1. 첫 페이지 - 전체 조회 (TIME_DESC)
    @Query("SELECT n FROM NotificationEntity n WHERE n.memberId = :memberId " +
           "ORDER BY n.createdAt DESC LIMIT :limit")
    List<NotificationEntity> findByMemberIdOrderByCreatedAtDesc(@Param("memberId") Long memberId, @Param("limit") int limit);

    // 2. 첫 페이지 - 전체 조회 (UNREAD_FIRST)
    @Query("SELECT n FROM NotificationEntity n WHERE n.memberId = :memberId " +
           "ORDER BY CASE WHEN n.status = 'UNREAD' THEN 0 ELSE 1 END, n.createdAt DESC LIMIT :limit")
    List<NotificationEntity> findByMemberIdOrderByUnreadFirstThenCreatedAtDesc(@Param("memberId") Long memberId, @Param("limit") int limit);

    // 3. 첫 페이지 - 상태 필터링 (TIME_DESC)
    @Query("SELECT n FROM NotificationEntity n WHERE n.memberId = :memberId AND n.status = :status " +
           "ORDER BY n.createdAt DESC LIMIT :limit")
    List<NotificationEntity> findByMemberIdAndStatusOrderByCreatedAtDesc(
            @Param("memberId") Long memberId, @Param("status") ReadStatus status, @Param("limit") int limit);

    // 4. 첫 페이지 - 상태 필터링 (UNREAD_FIRST)
    @Query("SELECT n FROM NotificationEntity n WHERE n.memberId = :memberId AND n.status = :status " +
           "ORDER BY CASE WHEN n.status = 'UNREAD' THEN 0 ELSE 1 END, n.createdAt DESC LIMIT :limit")
    List<NotificationEntity> findByMemberIdAndStatusOrderByUnreadFirstThenCreatedAtDesc(
            @Param("memberId") Long memberId, @Param("status") ReadStatus status, @Param("limit") int limit);

    // 5. 다음 페이지 - 전체 조회 (TIME_DESC)
    @Query("SELECT n FROM NotificationEntity n WHERE n.memberId = :memberId AND n.id < :lastNotificationId " +
           "ORDER BY n.createdAt DESC LIMIT :limit")
    List<NotificationEntity> findByMemberIdAndIdLessThanOrderByCreatedAtDesc(
            @Param("memberId") Long memberId, @Param("lastNotificationId") Long lastNotificationId, @Param("limit") int limit);

    // 6. 다음 페이지 - 전체 조회 (UNREAD_FIRST)
    @Query("SELECT n FROM NotificationEntity n WHERE n.memberId = :memberId AND n.id < :lastNotificationId " +
           "ORDER BY CASE WHEN n.status = 'UNREAD' THEN 0 ELSE 1 END, n.createdAt DESC LIMIT :limit")
    List<NotificationEntity> findByMemberIdAndIdLessThanOrderByUnreadFirstThenCreatedAtDesc(
            @Param("memberId") Long memberId, @Param("lastNotificationId") Long lastNotificationId, @Param("limit") int limit);

    // 7. 다음 페이지 - 상태 필터링 (TIME_DESC)
    @Query("SELECT n FROM NotificationEntity n WHERE n.memberId = :memberId AND n.status = :status AND n.id < :lastNotificationId " +
           "ORDER BY n.createdAt DESC LIMIT :limit")
    List<NotificationEntity> findByMemberIdAndStatusAndIdLessThanOrderByCreatedAtDesc(
            @Param("memberId") Long memberId, @Param("status") ReadStatus status, 
            @Param("lastNotificationId") Long lastNotificationId, @Param("limit") int limit);

    // 8. 다음 페이지 - 상태 필터링 (UNREAD_FIRST)
    @Query("SELECT n FROM NotificationEntity n WHERE n.memberId = :memberId AND n.status = :status AND n.id < :lastNotificationId " +
           "ORDER BY CASE WHEN n.status = 'UNREAD' THEN 0 ELSE 1 END, n.createdAt DESC LIMIT :limit")
    List<NotificationEntity> findByMemberIdAndStatusAndIdLessThanOrderByUnreadFirstThenCreatedAtDesc(
            @Param("memberId") Long memberId, @Param("status") ReadStatus status, 
            @Param("lastNotificationId") Long lastNotificationId, @Param("limit") int limit);
} 