import {
  PopupDetailRequestDto,
  PopupDetailResponseDto,
} from '@/entities/detail/types/type';
import { POPUP_DETAIL_ENDPOINTS } from '@/entities/detail/api/endpoints';

import { APIBuilder, logError } from '@/shared/lib';
import { ApiError } from 'next/dist/server/api-utils';

export const getDetailPopupApi = async (
  params: PopupDetailRequestDto
): Promise<PopupDetailResponseDto> => {
  try {
    const response = await APIBuilder.get(POPUP_DETAIL_ENDPOINTS)
      .timeout(5000)
      .setCache('force-cache')
      .params({ ...params })
      .build()
      .call<PopupDetailResponseDto>();

    return response.data;
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error, '팝업 상세 정보 조회');
    }
    throw error;
  }
};
