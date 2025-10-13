'use client';

import Link from 'next/link';
import Image, { StaticImageData } from 'next/image';
import React, { useEffect, useState } from 'react';
import { PopupCardViewProps } from '@/entities/popup/types/PopupListItem';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import IconBracketRight from '@/assets/icons/Normal/Icon_Bracket_Right.svg';
import DefaultImage from '/public/images/default-popup-image.png';
import dateToString from '@/entities/popup/lib/dateToString';

type ImgSrc = string | StaticImageData;
const PopupCardLink = ({
  children,
  linkTo,
}: {
  children: React.ReactNode;
  linkTo: string;
}) => (
  <Link href={linkTo} className={'group'}>
    <div className="relative w-full flex rounded-2xl bg-white overflow-hidden shadow-card border border-gray40">
      {children}
    </div>
  </Link>
);

const PopupCardImage = ({
  image,
  popupName,
  badge,
}: {
  image: string;
  popupName: string;
  badge?: React.ReactNode;
}) => {
  // 초기값: 값이 없으면 폴백
  const initial: ImgSrc = image
    ? `${process.env.NEXT_PUBLIC_API_IMAGE}${image}`
    : DefaultImage;

  const [src, setSrc] = useState<ImgSrc>(initial);
  const [errored, setErrored] = useState(false);

  // props.image가 바뀌면 초기화
  useEffect(() => {
    setErrored(false);
    setSrc(`${process.env.NEXT_PUBLIC_API_IMAGE}${image}` || DefaultImage);
  }, [image]);

  // src가 바뀌면 강제 리마운트 (Next/Image 내부 캐시/재시도 케이스 방지)
  const key = typeof src === 'string' ? src : src.src;
  return (
    <div className="relative w-[140px] min-h-[144px] overflow-hidden">
      <Image
        key={key}
        src={src}
        alt={`${popupName}-popup-image`}
        className="w-[140px] h-[144px] object-cover  group-hover:scale-105 transition-transform group-duration-200"
        width={140}
        height={144}
        onError={() => {
          if (!errored) {
            setErrored(true);
            setSrc(DefaultImage);
          }
        }}
        onLoadingComplete={img => {
          if (img.naturalWidth === 0 && !errored) {
            setErrored(true);
            setSrc(DefaultImage);
          }
        }}
      />
      {badge}
    </div>
  );
};

const PopupCardContent = ({
  location,
  popupName,
  period,
  waitingCount,
  dDay,
  type,
  registeredAt,
}: Pick<
  PopupCardViewProps,
  | 'location'
  | 'popupName'
  | 'period'
  | 'waitingCount'
  | 'dDay'
  | 'type'
  | 'registeredAt'
>) => {
  const isExpired = dDay < 0;

  const renderStatusText = () => {
    if (type === 'HOME' && isExpired) {
      return (
        <span className="text-[14px] font-light text-gray60">
          운영이 종료되었어요.
        </span>
      );
    }

    if (type === 'HISTORY' && registeredAt !== undefined) {
      const date = new Date(registeredAt);
      const dateString = dateToString(date);
      return (
        <span className="text-sm font-light text-gray60">
          {dateString} 방문 완료
        </span>
      );
    }

    if (waitingCount !== undefined) {
      return (
        <span className="text-sm font-light text-gray60">
          현재{' '}
          <strong className="text-main font-medium">{waitingCount}팀</strong>{' '}
          웨이팅 중
        </span>
      );
    }

    return null;
  };

  return (
    <div className="relative flex flex-1 flex-col justify-between py-3 pl-4">
      <div className="flex flex-col gap-y-[3px]">
        <h3 className="text-black font-medium text-base">{popupName}</h3>
        <div className="block font-medium text-gray60 text-sm tracking-tight">
          {period}
        </div>

        <p className="flex items-center font-medium text-sm text-gray60 gap-x-[3px]">
          <IconMap width={22} height={30} fill={'var(--color-gray60)'} />
          <span>{location}</span>
        </p>
      </div>

      {/** 1.접속 일자가 기간 내일 때 & 기본 : 현재 WaitingCount 명 웨이팅 중*/}
      {/** 2. 접속 일자가 기간 내 일때 & 방문 완료일 때 & 내방문 페이지에서(type===HISTORY) :0월 0일 방문 완료(registerdAt)*/}
      {/** 3. 접속 일자가 period의 마침 날짜 이후일 때 : 운영이 종료되었어요. (dDay로 판단 - isExpired)*/}
      {renderStatusText()}
    </div>
  );
};

const PopupCardRightBar = () => (
  <div className="absolute top-0 right-0 w-[24px] h-full bg-main flex items-center justify-center rounded-r-2xl">
    <IconBracketRight width={24} height={24} fill={'var(--color-white)'} />
  </div>
);

export default function PopupCardView(props: PopupCardViewProps) {
  return (
    <PopupCardLink linkTo={props.linkTo}>
      <PopupCardImage
        image={props.popupImageUrl}
        popupName={props.popupName}
        badge={props.Badge}
      />
      <PopupCardContent
        location={props.location}
        popupName={props.popupName}
        period={props.period}
        waitingCount={props.waitingCount}
        dDay={props.dDay}
        type={props.type}
        registeredAt={props.registeredAt}
      />
      {props.hasRightBar && <PopupCardRightBar />}
    </PopupCardLink>
  );
}
