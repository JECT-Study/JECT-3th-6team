import {
  MapPopupListRequestDto,
  MapPopupListResponseDto,
} from '@/entities/map/types/type';
import { MAP_ENDPOINTS } from '@/entities/map/api/endpoints';
import { APIBuilder, logError } from '@/shared/lib';
import { ApiError } from 'next/dist/server/api-utils';

export const getMapPopupListApi = async (
  params: MapPopupListRequestDto
): Promise<MapPopupListResponseDto> => {
  try {
    const response = await APIBuilder.get(MAP_ENDPOINTS.GET_POPUP_LIST)
      .timeout(5000)
      .setCache('force-cache')
      .params({ ...params })
      .build()
      .call<MapPopupListResponseDto>();

    return response.data;
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error, '지도 내 팝업리스트 불러오는 과정');
    }
    throw error;
  }
};
