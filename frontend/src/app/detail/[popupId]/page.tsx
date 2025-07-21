'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';

import { Badge } from '@/shared/ui/badge/Badge';
import { Tag } from '@/shared/ui/tag/Tag';
import { CircleMap } from '@/shared/ui';
import { BottomButtonContainer } from '@/shared/ui';
import PageHeader from '@/shared/ui/header/PageHeader';
import StandardButton from '@/shared/ui/button/StandardButton';
import { MediumText } from '@/shared/ui/text/MediumText';
import { SemiBoldText } from '@/shared/ui/text/SemiBoldText';
import type { _MapProps } from 'react-kakao-maps-sdk';

import { DescriptionTab } from '@/features/detail/ui/DescriptionTab';
import { ImageCarousel } from '@/features/detail/ui/ImageCarousel';

import IconClock from '@/assets/icons/Normal/Icon_Clock.svg';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';

import { popupDetailData } from './data';

export default function ProductDetail() {
  const router = useRouter();
  const {
    thumbnails,
    dDay,
    title,
    searchTags,
    location,
    period,
    status,
    brandStory,
    popupDetail,
  } = popupDetailData;

  type ClickMapFunction = _MapProps['onClick'];
  const handleClickMap: ClickMapFunction = (_, mouseEvent) => {
    const latlng = mouseEvent.latLng;
    const lat = latlng.getLat();
    const lng = latlng.getLng();
    router.push(`/map?lat=${lat}&lng=${lng}`);
  };

  return (
    <div className="pb-36">
      <PageHeader title="상세 정보" />
      {/* Image Carousel */}
      <ImageCarousel images={thumbnails} />

      {/* Main Detail */}
      <div className="py-6 px-5">
        {/* Badge and Rating */}
        <div className="flex items-center justify-between">
          <Badge iconPosition="left">
            {dDay > 0 ? `${dDay}일 남음` : '종료됨'}
          </Badge>
          <div className="flex items-center gap-1">
            <Image
              src="/icons/Normal/Icon_Star.svg"
              alt="star"
              width={18}
              height={17}
              className="w-4.5 h-4.5 fill-main"
            />
            <MediumText color="text-gray80">4.5</MediumText>
            <MediumText color="text-gray80">(25개의 리뷰)</MediumText>
          </div>
        </div>
        {/* Title and Tags*/}
        <div className="mt-6">
          <SemiBoldText size="lg">{title}</SemiBoldText>
          <Tag>{searchTags.type}</Tag>
          {searchTags.category.map((category, index) => (
            <Tag key={index}>{category}</Tag>
          ))}
        </div>
        {/* Schedule and Location */}
        <div className="flex items-center gap-2 mt-2.5">
          <IconClock
            width={22}
            height={22}
            fill={'var(--gray-80)'}
            stroke={'var(--gray-80)'}
          />
          <MediumText color="color-black">일정</MediumText>
          <MediumText color="color-black">|</MediumText>
          <MediumText color="color-black">
            {period.startDate} ~ {period.endDate}
          </MediumText>
        </div>
        <div className="flex items-center gap-2 mt-2.5">
          <IconMap
            width={22}
            height={22}
            fill={'var(--gray-80)'}
            stroke={'var(--gray-80)'}
          />
          <MediumText color="color-black">위치</MediumText>
          <MediumText color="color-black">|</MediumText>
          <MediumText color="color-black">{location.address_name}</MediumText>
        </div>
        {/* Map */}
        <div className="mt-2.5">
          <CircleMap
            center={{ lat: location.y, lng: location.x }}
            maxLevel={6}
            minLevel={6}
            onClick={handleClickMap}
          />
        </div>
      </div>

      {/* Description Section */}
      <div className="px-5 text-center">
        <SemiBoldText size="lg">팝업 설명</SemiBoldText>
      </div>
      <div className="w-full h-2 bg-main mt-2"></div>
      <div className="px-5">
        <DescriptionTab brandStory={brandStory} popupDetail={popupDetail} />
      </div>

      <BottomButtonContainer hasShadow={true}>
        <StandardButton
          onClick={() => {
            console.log('웨이팅');
          }}
          disabled={status === 'WAITING' || status === 'VISITED'}
          color="primary"
        >
          {status === 'WAITING'
            ? '예약중'
            : status === 'NONE'
              ? '웨이팅하기'
              : '방문 완료'}
        </StandardButton>
      </BottomButtonContainer>
    </div>
  );
}
