import {
  PopupHistoryListItemType,
  PopupListItemType,
} from '../types/PopupListItem';

// 얘역 내역 조회의 데이터를 팝업 리스트 데이터롭 변환
function adaptHistoryToDefaultData(
  data: PopupHistoryListItemType
): PopupListItemType {
  return {
    tag: 'DEFAULT',
    popupId: data.popup.popupId,
    popupName: data.popup.popupName,
    popupImageUrl: data.popup.popupImageUrl,
    waitingCount: data.waitingCount,
    location: {
      region1depthName: data.popup.location.region1depthName,
      region2depthName: data.popup.location.region2depthName,
      region3depthName: data.popup.location.region3depthName,
      addressName: data.popup.location.addressName,
      longitude: data.popup.location.longitude,
      latitude: data.popup.location.latitude,
    },
    period: data.popup.period,
    dDay: data.popup.dDay ?? 0, // 없으면 기본값 0
    searchTags: data.popup.searchTags,
  };
}

export default adaptHistoryToDefaultData;
