import { useRouter } from 'next/navigation';
import { NotificationCodeType } from '@/features/notification/type/Notification';
import NotificationCodeRouterMap from '@/features/notification/lib/notificationRouter';

export default function useNotificationNavigation() {
  const router = useRouter();

  return (code: NotificationCodeType, id: number | null) => {
    if (id === null) return;

    const nextPath = NotificationCodeRouterMap[code]?.(id);
    if (nextPath) router.push(nextPath);
  };
}
