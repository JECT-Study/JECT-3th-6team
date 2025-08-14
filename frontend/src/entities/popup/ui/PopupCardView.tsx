'use client';

import Link from 'next/link';
import Image, { StaticImageData } from 'next/image';
import React, { useEffect, useState } from 'react';
import {
  PopupCardViewProps,
  ratingType,
} from '@/entities/popup/types/PopupListItem';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import IconStar from '@/assets/icons/Normal/Icon_Star.svg';
import IconBracketRight from '@/assets/icons/Normal/Icon_Bracket_Right.svg';
import DefaultImage from '/public/images/default-popup-image.png';

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
  const initial: ImgSrc = image || DefaultImage;
  const [src, setSrc] = useState<ImgSrc>(initial);
  const [errored, setErrored] = useState(false);

  // props.image가 바뀌면 초기화
  useEffect(() => {
    setErrored(false);
    setSrc(image || DefaultImage);
  }, [image]);

  // src가 바뀌면 강제 리마운트 (Next/Image 내부 캐시/재시도 케이스 방지)
  const key = typeof src === 'string' ? src : src.src;

  return (
    <div className="relative w-[140px] min-h-[144px] overflow-hidden">
      <Image
        key={key}
        src={src}
        alt={`${popupName}-popup-image`}
        className="object-cover h-full group-hover:scale-105 transition-transform group-duration-200"
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
  rating,
  period,
  searchTags,
}: {
  location: string;
  popupName: string;
  rating?: ratingType;
  period: string;
  searchTags: string;
}) => (
  <div className="relative flex flex-1 flex-col justify-between py-3 pl-4">
    <div className="flex flex-col gap-y-[3px]">
      <p className="flex items-center font-medium text-sm text-gray60 gap-x-1">
        <IconMap width={22} height={30} fill={'var(--color-gray60)'} />
        <span>{location}</span>
      </p>
      <h3 className="text-black font-medium text-base">{popupName}</h3>
      <p className="text-gray60 font-regular text-[14px]">{searchTags}</p>
      {rating && (
        <p className="font-regular text-sm/normal text-gray60 flex gap-x-1 items-center">
          <IconStar width={16} height={16} fill={'var(--color-main)'} />
          <span>{rating.averageStar}</span>
          <span>({rating.reviewCount})</span>
        </p>
      )}
    </div>
    <div className="block font-medium text-gray60 text-sm tracking-tight">
      {period}
    </div>
  </div>
);

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
        rating={props.rating}
        period={props.period}
        searchTags={props.searchTags}
      />
      {props.hasRightBar && <PopupCardRightBar />}
    </PopupCardLink>
  );
}
