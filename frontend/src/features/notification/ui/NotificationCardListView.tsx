'use client';

import NotificationCard from '@/features/notification/ui/NotificationCard';
import useNotificationNavigation from '@/features/notification/hook/useNotificationNavigation';
import NotificationType from '@/features/notification/type/Notification';
import EmptyNotificationView from '@/features/notification/ui/EmptyNotificationView';

interface NotificationCardListViewProps {
  data: NotificationType[];
}

export default function NotificationCardListView({
  data,
}: NotificationCardListViewProps) {
  const handleClick = useNotificationNavigation();

  if (data.length === 0) return <EmptyNotificationView />;

  return (
    <div className={'px-[20px] flex flex-col gap-[12px] mt-[20px]'}>
      {data.map(notification => {
        const {
          notificationId,
          relatedResource: { id },
          notificationCode: code,
        } = notification;
        return (
          <NotificationCard
            data={notification}
            key={notificationId}
            onClick={() => handleClick(code, id)}
            onClose={() => console.log('close')}
          />
        );
      })}
    </div>
  );
}
