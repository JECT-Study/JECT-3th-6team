export interface PopupDetailRequestDto {
  popupId: number;
}

export interface PopupDetailSearchTags {
  type: string;
  category: string[];
}

export interface PopupDetailLocation {
  addressName: string;
  region1depthName: string;
  region2depthName: string;
  region3depthName: string;
  longitude: number;
  latitude: number;
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

export interface PopupDetailResponseDto extends PopupDetailItem {}
