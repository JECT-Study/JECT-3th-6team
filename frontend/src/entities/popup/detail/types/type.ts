export interface PopupDetailRequestDto {
  minLatitude: number;
  maxLatitude: number;
  minLongitude: number;
  maxLongitude: number;
  type?: string;
  category?: string;
  startDate?: string;
  endDate?: string;
}

export interface PopupDetailSearchTags {
  type: string;
  category: string[];
}

export interface PopupDetailLocation {
  address_name: string;
  region1depth_name: string;
  region2depth_name: string;
  region3depth_name: string;
  x: number;
  y: number;
}

export interface PopupDetailPeriod {
  startDate: string;
  endDate: string;
}

export interface PopupDetailBrandStory {
  imageUrls: string[];
  sns: {
    icon: string;
    url: string;
  }[];
}

export interface PopupDetailPopupDetail {
  dayOfWeeks: {
    dayOfWeek: string;
    value: string;
  }[];
  descriptions: string[];
}

type PopupDetailStatus = 'NONE' | 'WAITING' | 'VISITED';

export interface PopupDetailItem {
  id: number;
  thumbnails: string[];
  dDay: number;
  title: string;
  searchTags: PopupDetailSearchTags;
  location: PopupDetailLocation;
  period: PopupDetailPeriod;
  brandStory: PopupDetailBrandStory;
  popupDetail: PopupDetailPopupDetail;
  status: PopupDetailStatus;
}

export interface PopupDetailResponseDto {
  popupDetail: PopupDetailItem[];
}
