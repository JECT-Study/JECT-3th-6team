'use client';

import { useSuspenseQuery } from '@tanstack/react-query';

import { PopupHistoryListItemType } from '@/entities/popup/types/PopupListItem';
import { getWaitingDetailApi } from '@/features/waiting/api/getWaitingDetailApi';

export default function useWaitingDetail(waitingId: number) {
  const query = useSuspenseQuery<PopupHistoryListItemType>({
    queryKey: ['popup-history', 'detail', waitingId],
    queryFn: () => getWaitingDetailApi(waitingId),
    gcTime: 1000 * 60 * 30, // 30분
    staleTime: 1000 * 60 * 10, // 10분
    retry: 1,
  });

  return query;
}
