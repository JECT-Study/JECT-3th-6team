import { APIBuilder } from '@/shared/lib';
import { POPUP_DETAIL_ENDPOINTS } from '@/entities/popup/detail/api/endpoints';
import { PopupDetailResponseDto } from '@/entities/popup/detail/types/type';
import { ApiError } from 'next/dist/server/api-utils';

export const getPopupDetailApi = async (popupId: string) => {
  const response = await APIBuilder.get(
    POPUP_DETAIL_ENDPOINTS.GET_POPUP_DETAIL(popupId)
  )
    .timeout(5000)
    .setCache('force-cache')
    .build()
    .call<PopupDetailResponseDto>();

  return response.data.popupDetail[0]; // 배열에서 첫 번째 아이템 반환
};
