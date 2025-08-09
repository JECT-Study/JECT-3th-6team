export interface PopupDetailRequestDto {
  popupId: number;
}

interface searchTagsType {
  type: string;
  category: string[];
}

interface locationType {
  address_name: string; //전체 주소명
  region_1depth_name: string; //광역 시/도명
  region_2depth_name: string; //시/군/구명
  region_3depth_name: string; //읍/면/동명 (없을 경우 빈 문자열)
  longitude: number; //경도
  latitude: number; //위도
}

interface periodType {
  startDate: string; //시작일 (YYYY.MM.DD)
  endDate: string; //종료일 (YYYY.MM.DD)
}

interface snsType {
  icon: string; // (Facebook, Instagram, ...)
  url: string; // (https://www.instagram.com/your_page)
}

interface brandStoryType {
  imageUrls: string[]; // 브랜드 스토리 관련 이미지 URL
  sns: snsType[]; // 브랜드 스토리 설명 (HTML 형식으로 제공될 수 있음)
}

type popupStatusType = 'WAITING' | 'NONE' | 'VISITED';

interface dayOfWeeksType {
  dayOfWeek: string;
  value: string;
}

interface popupDetailType {
  dayOfWeeks: dayOfWeeksType[];
  descriptions: string[];
}

export interface PopupDetailItem {
  id: number; //팝업의 고유 식별자
  thumbnails: string[]; //팝업 썸네일 이미지 URL 목록
  dDay: number; //팝업 종료까지 남은 일수 // 0이면 마지막일, 음수면 종료.
  title: string; //팝업 이름
  searchTags: searchTagsType; //팝업 검색 태그 정보
  location: locationType; //팝업 위치 정보
  period: periodType; //팝업 운영 기간 (팝업 리스트와 동일)
  brandStory: brandStoryType; //브랜드 스토리 정보
  popupDetail: popupDetailType; //팝업 상세 설명
  status: popupStatusType; //사용자의 예약 상태 (비로그인 포함하여)
}

export interface PopupDetailResponseDto {
  popupList: PopupDetailItem[];
}
