export interface PopupDetailRequestDto {
  popupId: number;
}

interface searchTagsType {
  type: string;
  category: string[] | string;
}

interface locationType {
  address_name: string; //전체 주소명
  region_1depth_name: string; //광역 시/도명
  region_2depth_name: string; //시/군/구명
  region_3depth_name: string; //읍/면/동명 (없을 경우 빈 문자열)
  x: number; //경도
  y: number; //위도
}

interface periodType {
  startDate: string; //시작일 (YYYY-MM-DD)
  endDate: string; //종료일 (YYYY-MM-DD)
}

interface brandStoryType {
  imageUrls: string[]; // 브랜드 스토리 관련 이미지 URL
  description: string; // 브랜드 스토리 설명 (HTML 형식으로 제공될 수 있음)
}

type popupStatusType = 'WAITING' | 'NONE' | 'VISITED';

interface popupDetailType {
  dayOfWeeks: {
    dayOfWeek: string;
    value: string;
  }[];
  descriptions: string;
}

export interface PopupDetailItem {
  id: number; //팝업의 고유 식별자
  name: string; //팝업 이름
  images: string[] | string; //팝업 상세 이미지 URL 목록
  // 0이면 마지막일, 음수면 종료.
  dDay: number; //팝업 종료까지 남은 일수
  searchTags: searchTagsType; //팝업 검색 태그 정보
  location: locationType; //팝업 위치 정보
  period: periodType; //팝업 운영 기간 (팝업 리스트와 동일)
  brandStory: brandStoryType; //팝업 브랜드 스토리 정보
  // sns: [
  //     {
  //       icon: 'http://icon.com';
  //       url: 'http://url.com';
  //     },
  //   ];
  popupDetail: //팝업 상세 설명
  {
    // dayOfWeeks: [
    //   {
    //     dayOfWeek: 'MONDAY';
    //     value: '11:00 ~ 23:00';
    //   },
    // ];
    descriptions: string; //팝업 상세 내용 (HTML 형식으로 제공될 수 있음)
    // 이거 왜 string..?
  };
  status: popupStatusType;
}

export interface PopupDetailResponseDto {
  popupList: PopupDetailItem[];
}
