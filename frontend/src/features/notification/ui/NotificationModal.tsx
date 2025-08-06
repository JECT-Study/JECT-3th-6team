'use client';
import { useNotificationStore } from '@/features/notification/hook/useNotificationStore';
import { ModalContainer } from '@/shared/ui';
import NotificationCard from './NotificationCard';
import useNotificationNavigation from '@/features/notification/hook/useNotificationNavigation';
import { useEffect, useState } from 'react';

export default function NotificationModal() {
  const [isOpen, setOpen] = useState<boolean>(false);
  const notiList = useNotificationStore(state => state.list);
  const remove = useNotificationStore(state => state.remove);
  const handleCardClick = useNotificationNavigation();

  useEffect(() => {
    if (notiList.length > 0) {
      setOpen(true);
    } else {
      setOpen(false);
    }
  }, [notiList]);

  return (
    <ModalContainer open={isOpen} className={'items-start'}>
      <div
        className={
          'flex flex-col gap-y-2 px-5 pt-4.5 min-w-[320px] max-w-[430px] mx-auto '
        }
      >
        {notiList.map(noti => (
          <NotificationCard
            key={noti.notificationId}
            data={noti}
            onClose={() => remove(noti.notificationId)}
            onClick={() =>
              handleCardClick(noti.notificationCode, noti.relatedResource.id)
            }
          />
        ))}
      </div>
    </ModalContainer>
  );
}
