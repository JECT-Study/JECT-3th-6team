import { APIBuilder } from '@/shared/lib';
import { POPUP_DETAIL_ENDPOINTS } from '@/entities/popup/detail/api/endpoints';
import {
  PopupDetailResponseDto,
  PopupDetailRequestDto,
} from '@/entities/popup/detail/types/type';

export const getPopupDetailApi = async (
  popupId: PopupDetailRequestDto['popupId']
): Promise<PopupDetailResponseDto> => {
  const response = await APIBuilder.get(
    POPUP_DETAIL_ENDPOINTS.GET_POPUP_DETAIL(popupId)
  )
    .timeout(5000)
    .setCache('force-cache')
    .build()
    .call<PopupDetailResponseDto>();

  return response.data;
};
