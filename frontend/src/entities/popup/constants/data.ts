import { PopupItemType } from '@/entities/popup/types/PopupListItem';

const popupList: PopupItemType[] = [
  {
    tag: 'DEFAULT',
    id: 1,
    name: '팝업스토어 서울역점',
    location: {
      address_name: '서울특별시 용산구 청파로 378',
      region_1depth_name: '서울특별시',
      region_2depth_name: '용산구',
      region_3depth_name: '청파동',
      x: 126.9707,
      y: 37.5546,
    },
    rating: {
      averageStar: 4.5,
      reviewCount: 120,
    },
    period: {
      startDate: '2025-07-01',
      endDate: '2025-07-10',
    },
    dDay: 5,
    imageUrl: 'https://picsum.photos/200',
  },
  {
    tag: 'DEFAULT',
    id: 2,
    name: '성수 핫플 팝업',
    location: {
      address_name: '서울특별시 성동구 연무장길 31',
      region_1depth_name: '서울특별시',
      region_2depth_name: '성동구',
      region_3depth_name: '성수동',
      x: 127.056,
      y: 37.5446,
    },
    rating: {
      averageStar: 4.8,
      reviewCount: 98,
    },
    period: {
      startDate: '2025-07-03',
      endDate: '2025-07-12',
    },
    dDay: 7,
    imageUrl: 'https://picsum.photos/200',
  },
  {
    tag: 'HISTORY',
    waitingId: 101,
    popupId: 1,
    popupName: '레트로 감성 팝업',
    popupImageUrl: 'https://picsum.photos/200',
    location: '서울특별시 마포구 서교동 123-45',
    rating: {
      averageStar: 4.7,
      reviewCount: 85,
    },
    period: '2025년 6월 10일 ~ 6월 20일',
    waitingNumber: 12,
    status: 'COMPLETED',
  },
  {
    tag: 'HISTORY',
    waitingId: 102,
    popupId: 2,
    popupName: '여름 한정 팝업스토어',
    popupImageUrl: 'https://picsum.photos/200',
    location: '서울특별시 강남구 논현로 508',
    rating: {
      averageStar: 4.5,
      reviewCount: 110,
    },
    period: '2025년 7월 1일 ~ 7월 7일',
    waitingNumber: 8,
    status: 'RESERVED',
  },
  {
    tag: 'HISTORY',
    waitingId: 103,
    popupId: 3,
    popupName: '제로 웨이스트 팝업',
    popupImageUrl: 'https://picsum.photos/200',
    location: '경기도 성남시 분당구 정자동 77-1',
    rating: {
      averageStar: 4.3,
      reviewCount: 65,
    },
    period: '2025년 6월 25일 ~ 7월 3일',
    waitingNumber: 5,
    status: 'COMPLETED',
  },
];
export default popupList;
