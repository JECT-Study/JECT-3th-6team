'use client';

import useNotificationList from '@/features/notification/hook/useNotificationList';
import { useIntersectionObserver } from '@/shared/hook/useIntersectionObserver';
import NotificationCardListView from '@/features/notification/ui/NotificationCardListView';
import useDeleteNotification from '@/features/notification/hook/useDeleteNotification';

export default function NotificationCardList() {
  const { data, hasNextPage, isFetchingNextPage, fetchNextPage } =
    useNotificationList();
  const { mutate: deleteNotification } = useDeleteNotification();

  const lastElementRef = useIntersectionObserver(() => {
    if (hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  });

  return (
    <div className="flex flex-col">
      <NotificationCardListView
        data={data.content}
        handleDelete={deleteNotification}
        lastElementRef={lastElementRef}
      />
    </div>
  );
}
