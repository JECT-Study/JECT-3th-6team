import { useQuery } from '@tanstack/react-query';
import { PopupHistoryListItemType } from '@/entities/popup/types/PopupListItem';
import { useUserStore } from '@/entities/user/lib/useUserStore';
import { getWaitingNumberApi } from '@/features/waiting/api/getWaitingNumberApi';

export default function useWaitingNumber() {
  const isLoggedIn = useUserStore(state => state.userState.isLoggedIn);

  return useQuery<PopupHistoryListItemType>({
    queryKey: ['popup-history', 'detail', 'waiting'],
    queryFn: () => getWaitingNumberApi(),
    enabled: isLoggedIn,
    staleTime: 1000 * 30, // 30초
    gcTime: 1000 * 60 * 3, // 3분
    retry: 1,
    refetchInterval: query => {
      const data = query.state.data;
      if (data && data.status === 'WAITING') return 10000;

      // 데이터가 없을 경우 서버 요청 시간이 지수적으로 증가, 최대 10분
      const attempt = query.state.fetchFailureCount;
      // 0회: 30초, 1회: 60초, 2회: 120초 ...
      return Math.min(30000 * Math.pow(2, attempt), 600000);
    },
    throwOnError: false,
  });
}
