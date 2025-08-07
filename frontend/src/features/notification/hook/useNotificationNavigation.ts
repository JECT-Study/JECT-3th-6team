import { useRouter } from 'next/navigation';
import NotificationType from '@/features/notification/type/Notification';
import NotificationCodeRouterMap from '@/features/notification/lib/notificationRouter';
import { getIdFromNotification } from '@/features/notification/lib/getIdFromNotification';
import useReadNotification from './useReadNotification';

export default function useNotificationNavigation() {
  const router = useRouter();
  const { mutate } = useReadNotification();
  // handleClick 반환
  return (notification: NotificationType) => {
    const {
      notificationCode: code,
      relatedResources,
      isRead,
      notificationId,
    } = notification;
    // 1. 알림 읽음 처리.
    if (!isRead) {
      mutate(notificationId);
    }

    // 2. 연결된 페이지 이동
    const waitingId = getIdFromNotification({
      relatedResource: relatedResources,
      type: 'WAITING',
    });

    const nextPath = NotificationCodeRouterMap[code]?.(waitingId!);
    if (nextPath) router.push(nextPath);
  };
}
