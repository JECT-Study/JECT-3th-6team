'use client';

import { useFilterContext } from '@/features/filtering/lib/FilterContext';
import { useSuspenseInfiniteQuery } from '@tanstack/react-query';
import getPopupListApi, {
  PopupListRequest,
  TaggedPopupListResponse,
} from '@/entities/popup/api/getPopupListApi';
import dateToSeperatedString from '@/entities/popup/lib/dateToSeperatedString';
import { useQueryEffects } from '@/shared/hook/useQueryEffect';
import handleNetworkError from '@/shared/lib/handleNetworkError';

export default function useFilteredPopupList() {
  const { filter } = useFilterContext();
  const {
    location,
    keyword: { popupType, category },
    date: { start, end },
  } = filter;

  const request: PopupListRequest = {
    region1DepthName: location,
    type: popupType,
    category,
    startDate: dateToSeperatedString(start, '-'),
    size: 10,
  };

  if (end !== null) {
    request.endDate = dateToSeperatedString(end, '-');
  }

  const query = useSuspenseInfiniteQuery({
    queryKey: ['popup', 'list', { ...request }],
    queryFn: ({ pageParam = null }) => {
      const listReq = pageParam
        ? { ...request, lastPopupId: pageParam }
        : request;
      return getPopupListApi({ ...listReq });
    },
    gcTime: 1000 * 60 * 300, // 30분
    staleTime: 1000 * 60 * 10, // 10분
    getNextPageParam: (lastPage: TaggedPopupListResponse) =>
      lastPage.hasNext ? lastPage.lastPopupId : null,
    initialPageParam: null,
  });

  if (query.isLoading) console.log('[isLoading]');

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
