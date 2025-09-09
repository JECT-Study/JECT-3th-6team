'use client';

import useNotificationList from '@/features/notification/hook/useNotificationList';
import { useIntersectionObserver } from '@/shared/hook/useIntersectionObserver';
import NotificationCardListView from '@/features/notification/ui/NotificationCardListView';
import useDeleteNotification from '@/features/notification/hook/useDeleteNotification';
import NotificationType from '@/features/notification/type/Notification';
import { useEffect, useRef } from 'react';

export default function NotificationCardList() {
  const currentDeleteIdRef = useRef<number | null>(null);
  const { data, hasNextPage, isFetchingNextPage, fetchNextPage } =
    useNotificationList();
  const { mutate: deleteNotification } = useDeleteNotification();

  const lastElementRef = useIntersectionObserver(() => {
    if (hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  });

  const handleDeleteClick = (notification: NotificationType) => {
    const { notificationId } = notification;

    // 1. 클릭 시작
    performance.mark(`notification_delete_${notificationId}_start`);

    // 2.상태 없데이트
    currentDeleteIdRef.current = notificationId; // 현재 삭제할 알림id 기록
    deleteNotification(notificationId);
  };

  // refetch 된 이후 ui 갱신 시점
  useEffect(() => {
    if (data) {
      const deleteId = currentDeleteIdRef.current;
      const stillExists = data.content.some(
        noti => noti.notificationId === deleteId
      );

      if (deleteId && !stillExists) {
        performance.mark(`notification_delete_${deleteId}_end`);
        performance.measure(
          `notification_delete_${deleteId}_latency`,
          `notification_delete_${deleteId}_start`,
          `notification_delete_${deleteId}_end`
        );

        const entries = performance.getEntriesByName(
          `notification_delete_${deleteId}_latency`
        );

        const duration = entries[0]?.duration;

        if (duration) {
          console.log(
            `[Perf] Notification ${deleteId} delete latency: ${duration} ms`
          );
        }
      }
    }
  }, [data]);

  return (
    <div className="flex flex-col">
      <NotificationCardListView
        data={data.content}
        handleDelete={handleDeleteClick}
        lastElementRef={lastElementRef}
      />
    </div>
  );
}
