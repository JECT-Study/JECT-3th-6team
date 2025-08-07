import { useRouter } from 'next/navigation';
import {
  NotificationCodeType,
  RelatedResourceType,
} from '@/features/notification/type/Notification';
import NotificationCodeRouterMap from '@/features/notification/lib/notificationRouter';
import { getIdFromNotification } from '@/features/notification/lib/getIdFromNotification';

export default function useNotificationNavigation() {
  const router = useRouter();

  return (
    code: NotificationCodeType,
    relatedResource: RelatedResourceType[]
  ) => {
    const waitingId = getIdFromNotification({
      relatedResource,
      type: 'WAITING',
    });

    const nextPath = NotificationCodeRouterMap[code]?.(waitingId!);
    if (nextPath) router.push(nextPath);
  };
}
