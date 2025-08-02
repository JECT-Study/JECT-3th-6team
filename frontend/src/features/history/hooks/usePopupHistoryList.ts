'use client';

import { useSuspenseQuery } from '@tanstack/react-query';
import getPopupHistoryListApi, {
  PopupHistoryListRequest,
} from '@/features/history/api/getPopupHistoryListApi';
import { useQueryEffects } from '@/shared/hook/useQueryEffect';
import handleNetworkError from '@/shared/lib/handleNetworkError';

export default function usePopupHistoryList(request: PopupHistoryListRequest) {
  const query = useSuspenseQuery({
    queryKey: ['popup-history', 'list'],
    queryFn: () => getPopupHistoryListApi({ ...request }),
    gcTime: 1000 * 60 * 300, // 30분
    staleTime: 1000 * 60 * 10, // 10분
  });

  useQueryEffects(query, {
    onSuccess: data => {
      console.log('[onSuccess]:', data);
    },
    onError: error => {
      handleNetworkError(error);
      console.error('[onError]:', error);
      throw error;
    },
    onSettled: (data, error) => {
      console.log('[onSettled]:', data, error);
    },
  });

  return query;
}
