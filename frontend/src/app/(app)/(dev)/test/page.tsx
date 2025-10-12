import WaitingCountView from '@/features/waiting/ui/WaitingCountView';
import { PopupHistoryListItemType } from '@/entities/popup/types/PopupListItem';
import PageHeader from '@/shared/ui/header/PageHeader';

export default function Page() {
  const dummyPopupHistoryItem: PopupHistoryListItemType = {
    tag: 'HISTORY',
    waitingId: 101,
    waitingNumber: 12,
    status: 'WAITING', // 예: 'WAITING' | 'COMPLETED' | 'CANCELED' 등
    name: '이정수',
    peopleCount: 2,
    contactEmail: 'jeongsu@example.com',
    registeredAt: '2025-10-09T14:23:00Z',
    waitingCount: 25,
    expectedWaitingTimeMinutes: 45,
    popup: {
      popupId: 501,
      popupName: '달빛 아래의 빈티지 마켓',
      popupImageUrl: 'https://example.com/images/vintage-market.jpg',
      location: {
        addressName: '서울특별시 마포구 연남동 223-14',
        region1depthName: '서울특별시',
        region2depthName: '마포구',
        region3depthName: '연남동',
        longitude: 126.92345,
        latitude: 37.56223,
      },
      dDay: 5,
      period: '2025-10-05 ~ 2025-10-20',
      searchTags: {
        type: 'LIFESTYLE',
        category: ['빈티지', '플리마켓', '서울'],
      }, // searchTagType이 배열이라면
    },
  };

  return (
    <>
      <PageHeader title={'대기 순번'} />
      <WaitingCountView data={dummyPopupHistoryItem} />
      {/*<WaitingBottomButtonContainer waitingId={101} />*/}
    </>
  );
}
