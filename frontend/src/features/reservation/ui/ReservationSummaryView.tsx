'use client';

import React from 'react';
import Image from 'next/image';
import DefaultImage from '/public/images/default-popup-image.png';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import { BottomButtonContainer, StandardButton } from '@/shared/ui';
import { useRouter } from 'next/navigation';
import { formatKoreanDateTime } from '@/entities/popup/lib/formatKoreanDateTime';
import { OnsiteReservationResponse } from '@/features/reservation/type/OnsiteReservationResponse';

type ContentBlockProps = {
  label: string;
  value: string | number;
};

export const ContentBlock: React.FC<ContentBlockProps> = ({
  label,
  value,
}: ContentBlockProps) => (
  <div className={'w-full grid grid-cols-5'}>
    <span
      className={
        'text-gray60 font-semibold text-sm col-start-1 col-end-3 text-start'
      }
    >
      {label}
    </span>
    <span className={'col-start-3 col-end-6 text-sm font-medium'}>{value}</span>
  </div>
);

export default function ReservationSummaryView({
  data,
}: {
  data: OnsiteReservationResponse;
}) {
  const { name, email, peopleCount, registeredAt } = data;
  const router = useRouter();
  const displayData = [
    { label: '대기자 명', value: name },
    { label: '대기자 수', value: peopleCount },
    { label: '대기자 이메일', value: email },
    { label: '대기 일자', value: formatKoreanDateTime(registeredAt) },
  ];

  return (
    <div>
      <div className={'w-full mt-[14px] px-5 flex flex-col gap-y-[26px]'}>
        {/*이미지와 팝업 이름*/}
        <div className={'flex items-center gap-x-[16px]'}>
          <Image
            src={data.popupImageUrl || DefaultImage}
            alt="popup image"
            width={80}
            height={80}
            className={'rounded-[14px] object-cover'}
          />
          <div className={'flex flex-col gap-y-2 items-start'}>
            <h2 className={'text-xl font-semibold text-black'}>
              {data.popupName}
            </h2>
            <p className={'text-sm text-medium flex items-center gap-x-1'}>
              <IconMap width={20} height={20} fill={'var(--color-gray60)'} />
              <span>{data.location.addressName}</span>
            </p>
          </div>
        </div>

        <hr className={'border-gray40'} />

        {/*예약 정보*/}
        <div className={'w-full flex flex-col gap-y-[30px] '}>
          {displayData.map(({ label, value }, index) => (
            <ContentBlock label={label} value={value} key={index} />
          ))}
        </div>
      </div>
      <BottomButtonContainer hasShadow={false}>
        <StandardButton
          onClick={() => router.push(`/`)}
          disabled={false}
          color={'primary'}
        >
          내 대기번호 확인하기
        </StandardButton>
      </BottomButtonContainer>
    </div>
  );
}
