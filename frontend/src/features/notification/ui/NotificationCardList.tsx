'use client';

import useNotificationList from '@/features/notification/hook/useNotificationList';
import { useIntersectionObserver } from '@/shared/hook/useIntersectionObserver';
import NotificationCardListView from '@/features/notification/ui/NotificationCardListView';

export default function NotificationCardList() {
  const { data, hasNextPage, isFetchingNextPage, fetchNextPage } =
    useNotificationList();
  const lastElementRef = useIntersectionObserver(() => {
    if (hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  });

  return (
    <div className="flex flex-col">
      <NotificationCardListView data={data.content} />
      {hasNextPage && <div ref={lastElementRef} className="h-4 " />}
    </div>
  );
}
