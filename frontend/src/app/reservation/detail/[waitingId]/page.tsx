import PageHeader from '@/shared/ui/header/PageHeader';
import React from 'react';
import { ContentBlock } from '@/features/reservation/ui/ReservationSummaryView';
import { PopupHistoryListItemType } from '@/entities/popup/types/PopupListItem';
import ReservationDetailCard from '@/entities/popup/ui/ReservationDetailCard';
import { formatKoreanDateTime } from '@/entities/popup/lib/formatKoreanDateTime';

export default async function ReservationDetailPage({
  params,
}: {
  params: Promise<{ waitingId: number }>;
}) {
  // TODO 예약내역 조회 api 연결
  const { waitingId } = await params;
  console.log('waitingId : ', waitingId, '');
  const data: PopupHistoryListItemType = {
    tag: 'HISTORY',
    waitingId: 1,
    waitingNumber: 1,
    status: 'WAITING',
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
      <PageHeader title={'예약 내역 확인'} />
      <div>
        <div className={'w-full mt-[14px] px-5 flex flex-col gap-y-[26px]'}>
          <ReservationDetailCard data={data} />
          <hr className={'border-gray40'} />
          {/*예약 정보*/}
          <div className={'w-full flex flex-col gap-y-[30px] '}>
            <ContentBlock label={'대기자 명'} value={data!.name} />
            <ContentBlock label={'대기자 수'} value={data!.peopleCount} />
            <ContentBlock label={'대기자 이메일'} value={data!.contactEmail} />
            <ContentBlock
              label={'대기 일자'}
              value={formatKoreanDateTime(data!.registeredAt)}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
