import Link from 'next/link';
import Image from 'next/image';
import React from 'react';
import { VisitedPopupListItemType } from '@/entities/popup/types/PopupListItem';

interface PopupCardProps {
  popupId: number;
  popupName: string;
  popupImageUrl: string;
  location: string;
  rating: {
    averageStar: number;
    reviewCount: number;
  };
  period: string;
  status?: Pick<VisitedPopupListItemType, 'status'>;
  Badge?: React.ElementType; // 뱃지는 외부 주입
}

export default function BasicPopupCard({
  popupId,
  popupName,
  popupImageUrl,
  location,
  rating,
  period,
  Badge,
}: PopupCardProps) {
  return (
    <Link href={`/popup/detail/${popupId}`}>
      <div className="w-full flex rounded-2xl bg-white overflow-hidden shadow-card">
        <div className={'relative w-[140px] min-h-[144px] overflow-hidden'}>
          {/*이미지*/}
          <Image
            src={popupImageUrl}
            alt={'popup image'}
            className={'object-cover h-full'}
            width={140}
            height={144}
          />
          {Badge && <Badge className={'absolute top-[12px] left-[8px]'} />}
        </div>

        <div
          className={'relative flex flex-1 flex-col justify-between py-4 pl-4'}
        >
          {/*팝업정보*/}
          <div className={'flex flex-col  gap-y-[8px]'}>
            <p className={'font-medium text-sm text-gray60 flex gap-x-1'}>
              <Image
                src={'/icons/Normal/Icon_Map.svg'}
                alt="location"
                width={12}
                height={13}
                className={'object-cover'}
              />
              <span>{location}</span>
            </p>
            <h3 className={'text-black font-semibold text-base '}>
              {popupName}
            </h3>
            <p
              className={
                'font-regular text-sm/normal text-gray60 flex gap-x-1 items-center'
              }
            >
              <Image
                src="/icons/Normal/Icon_Star.svg"
                alt="star"
                width={16}
                height={14}
                className={'object-cover'}
              />
              <span>{rating.averageStar}</span>
              <span>({rating.reviewCount})</span>
            </p>
          </div>
          <div
            className={'block font-semibold text-gray60 text-sm tracking-tight'}
          >
            {period}
          </div>
        </div>
      </div>
    </Link>
  );
}
