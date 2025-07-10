import React from 'react';

export type ratingType = {
  averageStar: number;
  reviewCount: number;
};
export type statusType = 'RESERVED' | 'COMPLETED';

// 태그가 부착된 타입
export type PopupItemType = PopupListItemType | PopupHistoryListItemType;
export type PopupListItemType = { tag: 'DEFAULT' } & RawPopupListItemType;
export type PopupHistoryListItemType = {
  tag: 'HISTORY';
} & RawPopupHistoryListItemType;

// 태그가 부착되지 않은 raw data union 타입
export type RawPopupItemType =
  | RawPopupListItemType
  | RawPopupHistoryListItemType;

/**
 * =========================
 *   홈화면 팝업 : DEFAULT
 * =========================
 */

interface RawPopupListItemType {
  id: number;
  name: string;
  location: {
    address_name: string;
    region_1depth_name: string;
    region_2depth_name: string;
    region_3depth_name: string;
    x: number;
    y: number;
  };
  rating: ratingType;
  period: {
    startDate: string;
    endDate: string;
  };
  dDay: number;
  imageUrl: string;
}

/**
 * =========================
 *   내 방문기록 팝업 : HISTORY
 * =========================
 */

interface RawPopupHistoryListItemType {
  waitingId: number;
  popupId: number;
  popupName: string;
  popupImageUrl: string;
  location: string;
  rating: ratingType;
  period: string;
  waitingNumber: number;
  status: statusType;
}

export interface PopupCardViewProps {
  popupId: number;
  popupName: string;
  popupImageUrl: string;
  location: string;
  rating: ratingType;
  period: string;
  linkTo: string;
  hasRightBar?: boolean;
  Badge?: React.ReactElement;
}
