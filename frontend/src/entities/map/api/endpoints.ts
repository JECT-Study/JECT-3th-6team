export const MAP_ENDPOINTS = {
  // 지도 내 팝업 목록 조회
  GET_POPUP_LIST: '/popups/map',

  // 팝업 상세 정보 조회
  GET_POPUP_DETAIL: (popupId: number) => `/popups/${popupId}`,
} as const;
