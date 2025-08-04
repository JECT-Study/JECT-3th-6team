export interface MapPopupListRequestDto {
  minLatitude: number;
  maxLatitude: number;
  minLongitude: number;
  maxLongitude: number;
  type?: string;
  category?: string;
  startDate?: string;
  endDate?: string;
}

export interface MapPopupItem {
  id: number;
  latitude: number;
  longitude: number;
}

export interface MapPopupListResponseDto {
  popupList: MapPopupItem[];
}
