import Link from 'next/link';
import Image from 'next/image';
import React from 'react';
import {
  PopupCardViewProps,
  ratingType,
} from '@/entities/popup/types/PopupListItem';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import IconStar from '@/assets/icons/Normal/Icon_Star.svg';
import IconBracketRight from '@/assets/icons/Normal/Icon_Bracket_Right.svg';

const PopupCardLink = ({
  children,
  linkTo,
}: {
  children: React.ReactNode;
  linkTo: string;
}) => (
  <Link href={linkTo}>
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
}) => (
  <div className="relative w-[140px] min-h-[144px] overflow-hidden">
    <Image
      src={image}
      alt={`${popupName}-popup-image`}
      className="object-cover h-full"
      width={140}
      height={144}
    />
    {badge}
  </div>
);

const PopupCardContent = ({
  location,
  popupName,
  rating,
  period,
}: {
  location: string;
  popupName: string;
  rating?: ratingType;
  period: string;
}) => (
  <div className="relative flex flex-1 flex-col justify-between py-4 pl-4">
    <div className="flex flex-col gap-y-[3px]">
      <p className="flex items-center font-medium text-sm text-gray60 gap-x-1">
        <IconMap width={22} height={30} fill={'var(--color-gray60)'} />
        <span>{location}</span>
      </p>
      <h3 className="text-black font-medium text-base">{popupName}</h3>
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
      />
      {props.hasRightBar && <PopupCardRightBar />}
    </PopupCardLink>
  );
}
