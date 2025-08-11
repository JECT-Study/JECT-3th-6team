'use client';

import BellIcon from '@/assets/icons/Normal/Icon_Bell.svg';
import Link from 'next/link';
import { useQuery } from '@tanstack/react-query';
import getNotificationsApi, {
  NotificationResponse,
} from '@/features/notification/api/getNotificationsApi';
import { useUserStore } from '@/entities/user/lib/useUserStore';
import useNotificationListener from '@/features/notification/hook/useNotificationListener';

const DefaultBell = () => {
  return (
    <Link href={'/notify'}>
      <BellIcon fill={'var(--color-main)'} width={28} height={28} />
    </Link>
  );
};

export default function NotificationBell() {
  const isLoggedIn = useUserStore(state => state.userState.isLoggedIn);
  useNotificationListener();

  const { data, isError, isLoading } = useQuery<NotificationResponse>({
    queryKey: ['notification', 'unread-list'],
    queryFn: () =>
      getNotificationsApi({
        size: 10,
        readStatus: 'UNREAD',
        sort: 'UNREAD_FIRST',
      }),
    staleTime: 1000 * 60, // 1분
    gcTime: 1000 * 60 * 5, // 5분
    retry: false,
    enabled: isLoggedIn,
    throwOnError: false,
  });

  if (isError) return <DefaultBell />;
  if (isLoading) return;

  return (
    <div className={'relative'}>
      <DefaultBell />
      {data?.content && data?.content?.length > 0 && (
        <span className="absolute top-0 right-0 flex size-2 z-10">
          <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-blue-200 opacity-85"></span>
          <span className="relative inline-flex size-2 rounded-full bg-red-500"></span>
        </span>
      )}
    </div>
  );
}
