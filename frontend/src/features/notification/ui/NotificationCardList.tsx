'use client';

import dummyNotificationList from '@/features/notification/api/DummyNotification';
import NotificationCard from '@/features/notification/ui/NotificationCard';
import useNotificationNavigation from '@/features/notification/hook/useNotificationNavigation';

export default function NotificationCardList() {
  const handleClick = useNotificationNavigation();

  return (
    <div className={'px-[20px] flex flex-col gap-[12px] mt-[20px]'}>
      {dummyNotificationList.map(notification => {
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
