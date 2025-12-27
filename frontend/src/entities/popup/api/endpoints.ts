/**
 * Popup API Endpoints
 */
export const POPUP_ENDPOINTS = {
  // 팝업 목록 조회
  GET_POPUP_LIST: '/popups',

  // 팝업 상세 정보 조회
  GET_POPUP_DETAIL: (popupId: number) => `/popups/${popupId}`,
} as const;

