'use client';

import useNotificationList from '@/features/notification/hook/useNotificationList';
import { useIntersectionObserver } from '@/shared/hook/useIntersectionObserver';
import NotificationCardListView from '@/features/notification/ui/NotificationCardListView';
import useDeleteNotification from '@/features/notification/hook/useDeleteNotification';
import NotificationType from '@/features/notification/type/Notification';
import { useRef } from 'react';

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

    // 2.상태 없데이트
    currentDeleteIdRef.current = notificationId; // 현재 삭제할 알림id 기록
    deleteNotification(notificationId);
  };

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
