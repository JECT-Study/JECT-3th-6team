'use client';

import React from 'react';
import Image from 'next/image';
import ExImage from '/public/images/popup-ex.png';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import { BottomButtonContainer, StandardButton } from '@/shared/ui';
import { useRouter } from 'next/navigation';

type ContentBlockProps = {
  label: string;
  value: string | number;
};

const ContentBlock: React.FC<ContentBlockProps> = ({
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

export default function ReservationSummaryView() {
  const router = useRouter();
  const displayData = [
    { label: '예약자 명', value: '이윤재' },
    { label: '예약자 수', value: 3 },
    { label: '예약자 이메일', value: 'asdf@gmail.coms' },
  ];

  return (
    <div>
      <div className={'w-full mt-[14px] px-5 flex flex-col gap-y-[26px]'}>
        {/*이미지와 팝업 이름*/}
        <div className={'flex items-center gap-x-[16px]'}>
          <Image
            src={ExImage}
            alt="popup image"
            width={80}
            height={80}
            className={'rounded-[14px] object-cover'}
          />
          <div className={'flex flex-col gap-y-2 items-start'}>
            <h2 className={'text-xl font-semibold text-black'}>
              젠틀 몬스터 팝업
            </h2>
            <p className={'text-sm text-medium flex items-center gap-x-1'}>
              <IconMap width={15} height={17} fill={'var(--color-gray60)'} />
              <span>서울, 용산구 한남동 61-2</span>
            </p>
          </div>
        </div>

        <hr className={'border-gray40'} />

        {/*예약 정보*/}
        <div className={'w-full flex flex-col gap-y-[40px] '}>
          {displayData.map(({ label, value }, index) => (
            <ContentBlock label={label} value={value} key={index} />
          ))}
        </div>
      </div>
      <BottomButtonContainer hasShadow={false}>
        <StandardButton
          onClick={() => router.push('/')}
          disabled={false}
          color={'primary'}
        >
          내 대기번호 확인하기
        </StandardButton>
      </BottomButtonContainer>
    </div>
  );
}
