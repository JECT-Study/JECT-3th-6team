import React from 'react';

export type ratingType = {
  averageStar: number;
  reviewCount: number;
};

export type searchTagType = {
  type: string;
  category: string[];
};

export type statusType = 'WAITING' | 'VISITED';
export type tagType = 'DEFAULT' | 'HISTORY';
interface TaggedItem<T extends tagType> {
  tag: T;
}
// 태그가 부착된 타입
export type PopupItemType = PopupListItemType | PopupHistoryListItemType;
export type PopupListItemType = TaggedItem<'DEFAULT'> & RawPopupListItemType;
export type PopupHistoryListItemType = TaggedItem<'HISTORY'> &
  RawPopupHistoryListItemType;

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
  rating?: ratingType;
  period: {
    startDate: string;
    endDate: string;
  };
  dDay: number;
  imageUrl: string;
  searchTags: searchTagType;
}

/**
 * =========================
 *   내 방문기록 팝업 : HISTORY
 * =========================
 */

interface RawPopupHistoryListItemType {
  waitingId: number;
  waitingNumber: number;
  status: statusType;
  name: string;
  peopleCount: number;
  contactEmail: string;
  registeredAt: string;
  popup: {
    popupId: number;
    popupName: string;
    popupImageUrl: string;
    location: {
      addressName: string;
      region1depthName: string;
      region2depthName: string;
      region3depthName: string;
      longitude: number;
      latitude: number;
    };
    dDay: number;
    period: string; // "2025-06-01 ~ 2025-06-25"
    searchTags: searchTagType;
  };
}

export interface PopupCardViewProps {
  popupId: number;
  popupName: string;
  popupImageUrl: string;
  location: string;
  period: string;
  linkTo: string;
  searchTags: string;
  hasRightBar?: boolean;
  Badge?: React.ReactElement;
  rating?: ratingType;
}
