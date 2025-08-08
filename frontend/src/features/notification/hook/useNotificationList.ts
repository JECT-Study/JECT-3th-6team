import { useSuspenseInfiniteQuery } from '@tanstack/react-query';
import getNotificationsApi, {
  NotificationRequest,
  NotificationResponse,
} from '@/features/notification/api/getNotificationsApi';
import { useQueryEffects } from '@/shared/hook/useQueryEffect';
import handleNetworkError from '@/shared/lib/handleNetworkError';

export default function useNotificationList() {
  const initialRequest: Omit<NotificationRequest, 'lastNotificationId'> = {
    size: 10,
    readStatus: 'ALL',
    sort: 'UNREAD_FIRST',
  };

  const query = useSuspenseInfiniteQuery({
    queryKey: ['notification', 'list'],
    queryFn: ({ pageParam = null }) => {
      const request =
        pageParam !== null
          ? { ...initialRequest, lastNotificationId: pageParam }
          : initialRequest;
      return getNotificationsApi({ ...request });
    },
    getNextPageParam: (lastPage: NotificationResponse) =>
      lastPage.hasNext ? lastPage.lastNotificationId : null,
    initialPageParam: null,
    gcTime: 1000 * 60 * 5, // 5분
    staleTime: 1000 * 60, // 1분
    retry: 1,
  });

  // TODO : 중복되는 로직 공통으로 빼기
  useQueryEffects(query, {
    onSuccess: data => {
      if (process.env.NEXT_PUBLIC_ENV === 'DEVELOP') {
        console.log('[onSuccess]:', data);
      }
    },
    onError: error => {
      handleNetworkError(error);
      console.error('[onError]:', error);
      throw error;
    },
    onSettled: (data, error) => {
      if (process.env.NEXT_PUBLIC_ENV === 'DEVELOP') {
        console.log('[onSettled]:', data, error);
      }
    },
  });

  return {
    ...query,
    data: query.data.pages[0],
  };
}
