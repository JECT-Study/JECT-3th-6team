'use client';

import { useQuery } from '@tanstack/react-query';
import { getPopupDetailApi } from '@/entities/popup/detail/api/api';
import PopupDetailContent from '@/features/detail/ui/PopupDetailContent';

interface PopupDetailContainerProps {
  popupId: number;
}

export default function PopupDetailContainer({
  popupId,
}: PopupDetailContainerProps) {
  const { data: popupDetailData } = useQuery({
    queryKey: ['popupDetail', popupId],
    queryFn: () => getPopupDetailApi(popupId),
  });

  console.log('PopupDetailData:', popupDetailData);

  return (
    <PopupDetailContent popupDetailData={popupDetailData} popupId={popupId} />
  );
}
