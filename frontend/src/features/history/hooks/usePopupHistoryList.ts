'use client';

import { useSuspenseInfiniteQuery } from '@tanstack/react-query';
import getPopupHistoryListApi, {
  TaggedPopupHistoryListResponse,
} from '@/features/history/api/getPopupHistoryListApi';
import { useQueryEffects } from '@/shared/hook/useQueryEffect';
import handleNetworkError from '@/shared/lib/handleNetworkError';

export default function usePopupHistoryList() {
  const initialRequest = {
    size: 10,
  };

  const query = useSuspenseInfiniteQuery({
    queryKey: ['popup-history', 'list'],
    queryFn: ({ pageParam = null }) => {
      const request =
        pageParam !== null
          ? { ...initialRequest, lastWaitingId: pageParam }
          : initialRequest;
      return getPopupHistoryListApi({ ...request });
    },
    gcTime: 1000 * 60 * 30, // 30분
    staleTime: 1000 * 60 * 10, // 10분,
    getNextPageParam: (lastPage: TaggedPopupHistoryListResponse) =>
      lastPage.hasNext ? lastPage.lastWaitingId : null,
    initialPageParam: null,
    retry: 1,
  });

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
  const mergedData = {
    ...query.data.pages[0],
    content: query.data.pages.flatMap(p => p.content),
  };
  return {
    ...query,
    data: mergedData,
  };
}
