import {
  PopupHistoryListItemType,
  PopupListItemType,
} from '../types/PopupListItem';
import { periodStringToDate } from '@/entities/popup/lib/dateToPeriodString';

// 얘역 내역 조회의 데이터를 팝업 리스트 데이터롭 변환
function adaptHistoryToDefaultData(
  data: PopupHistoryListItemType
): PopupListItemType {
  return {
    tag: 'DEFAULT',
    id: data.popup.popupId,
    name: data.popup.popupName,
    imageUrl: data.popup.popupImageUrl,
    location: {
      region_1depth_name: data.popup.location.region1depthName,
      region_2depth_name: data.popup.location.region2depthName,
      region_3depth_name: data.popup.location.region3depthName,
      address_name: data.popup.location.addressName,
      x: data.popup.location.longitude,
      y: data.popup.location.latitude,
    },
    period: {
      startDate: periodStringToDate(data.popup.period).startDate.toISOString(),
      endDate: periodStringToDate(data.popup.period).endDate.toISOString(),
    },
    dDay: data.popup.dDay ?? 0, // 없으면 기본값 0
    searchTags: data.popup.searchTags,
  };
}

export default adaptHistoryToDefaultData;
