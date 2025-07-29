'use client';

import { useFilterContext } from '@/features/filtering/lib/FilterContext';
import { useQuery } from '@tanstack/react-query';
import getPopupListApi, {
  PopupListRequest,
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

  const query = useQuery({
    queryKey: ['popup', 'list', { ...request }],
    queryFn: () => getPopupListApi({ ...request }),
    gcTime: 1000 * 60 * 300, // 30분
    staleTime: 1000 * 60 * 10, // 10분
  });

  if (query.isLoading) console.log('[isLoading]');

  useQueryEffects(query, {
    onSuccess: data => {
      console.log('[onSuccess]:', data);
    },
    onError: error => {
      handleNetworkError(error);
      console.error('[onError]:', error);
    },
    onSettled: (data, error) => {
      console.log('[onSettled]:', data, error);
    },
  });

  return query;
}
