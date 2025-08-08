import ReservationDetailCard from '@/entities/popup/ui/ReservationDetailCard';
import { ContentBlock } from '@/features/reservation/ui/ReservationSummaryView';
import { formatKoreanDateTime } from '@/entities/popup/lib/formatKoreanDateTime';
import React from 'react';
import { PopupHistoryListItemType } from '@/entities/popup/types/PopupListItem';

export default function ReservationDetailView({
  data,
}: {
  data: PopupHistoryListItemType;
}) {
  return (
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
  );
}
