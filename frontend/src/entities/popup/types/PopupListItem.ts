type ratingType = {
  averageStar: number;
  reviewCount: number;
};

// 팝업 리스트 조회시
export interface PopupListItemType {
  id: number;
  name: string;
  location: {
    address_name: string; // '경기도 성남시 분당구' 전체 주소명
    region_1depth_name: string; // '경기도' 광역시도 명
    region_2depth_name: string; // '성남시 분당구' 시/군/구
    region_3depth_name: string; // 음/면/동
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

// 방문 내역 조회시
export interface VisitedPopupListItemType {
  waitingId: number;
  popupId: number;
  popupName: string;
  popupImageUrl: 'https://example.com/images/seogu_design.jpg';
  location: string;
  rating: ratingType;
  period: string;
  waitingNumber: number;
  status: 'RESERVED' | 'COMPLETED';
}
