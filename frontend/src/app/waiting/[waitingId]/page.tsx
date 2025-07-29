import PageHeader from '@/shared/ui/header/PageHeader';
import WaitingBottomButtonContainer from '@/features/waiting/ui/WaitingBottomButtonContainer';
import WaitingCountView from '@/features/waiting/ui/WaitingCountView';
import { PopupHistoryListItemType } from '@/entities/popup/types/PopupListItem';

export default async function WaitingPage({
  params,
}: {
  params: Promise<{ waitingId: number }>;
}) {
  const { waitingId } = await params;

  const Dummy: PopupHistoryListItemType = {
    tag: 'HISTORY',
    waitingId: 1,
    waitingNumber: 1,
    status: 'VISITED',
    name: '임수빈',
    peopleCount: 4,
    contactEmail: 'robin980108@naver.com',
    registeredAt: '2025-06-26T16:00:00',
    popup: {
      popupId: 1,
      popupName: '무신사 X 나이키 팝업스토어',
      popupImageUrl: 'https://picsum.photos/200',
      location: {
        addressName: '경기도 성남시 분당구',
        region1depthName: '경기도',
        region2depthName: '성남시 분당구',
        region3depthName: '',
        longitude: 127.423084873712,
        latitude: 37.0789561558879,
      },
      dDay: -18,
      period: '2025-06-01 ~ 2025-06-25',
      searchTags: {
        type: '체험형',
        category: ['패션', '예술'],
      },
    },
  };
  return (
    <div>
      <PageHeader title={'대기 순번'} />
      <WaitingCountView data={Dummy} />
      <WaitingBottomButtonContainer waitingId={waitingId} />
    </div>
  );
}
