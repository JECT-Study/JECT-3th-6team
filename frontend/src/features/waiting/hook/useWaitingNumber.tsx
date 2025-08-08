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
    refetchInterval: 10000, // 10초마다 재요청
    throwOnError: false,
  });
}
