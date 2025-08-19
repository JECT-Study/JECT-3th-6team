'use client';

import { useQuery } from '@tanstack/react-query';
import { getPopupDetailApi } from '@/entities/popup/detail/api/api';
import PopupDetailContent from './PopupDetailContent';

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

  if (!popupDetailData) {
    throw new Error('팝업 데이터를 찾을 수 없습니다.');
  }

  return (
    <PopupDetailContent popupDetailData={popupDetailData} popupId={popupId} />
  );
}
