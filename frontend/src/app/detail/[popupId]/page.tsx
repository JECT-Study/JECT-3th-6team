'use client';

import { Badge } from '@/shared/ui/badge/Badge';
import { Tag } from '@/shared/ui/tag/Tag';
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { DescriptionTab } from '@/features/detail/ui/DescriptionTab';
import { ReviewTab } from '@/features/detail/ui/ReviewTab';
import { ImageCarousel } from '@/features/detail/ui/ImageCarousel';
import { SemiBoldText } from '@/shared/ui/text/SemiBoldText';
import { MediumText } from '@/shared/ui/text/MediumText';
import Image from 'next/image';
import IconClock from '@/assets/icons/Normal/Icon_Clock.svg';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import { CircleMap } from '@/shared/ui';
import PageHeader from '@/shared/ui/header/PageHeader';
import StandardButton from '@/shared/ui/button/StandardButton';
import { BottomButtonContainer } from '@/shared/ui';
export default function ProductDetail() {
  const images = [
    '/images/sunglass.jpg',
    '/images/sunglass.jpg',
    '/images/sunglass.jpg',
  ];

  return (
    <div className="pb-36">
      <PageHeader title="상세 정보" />
      {/* Image Carousel */}
      <ImageCarousel images={images} />

      {/* Main Detail */}
      <div className="py-6 px-5">
        {/* Badge and Rating */}
        <div className="flex items-center justify-between">
          <Badge iconPosition="left">10일 남음</Badge>
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
          <SemiBoldText size="lg">젠틀몬스터</SemiBoldText>
          <Tag>수도권</Tag>
          <Tag>체험형</Tag>
          <Tag>패션</Tag>
          <Tag>뷰티</Tag>
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
            25.06.01(월) ~ 25.06.30(수)
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
          <MediumText color="color-black">서울, 용산구 한남동 61-2</MediumText>
        </div>
        {/* Map */}
        <div className="mt-2.5">
          <CircleMap
            center={{ lat: 37.5353, lng: 127.008 }}
            radius={200}
            maxLevel={6}
            minLevel={6}
          />
        </div>
      </div>

      {/* Description Section */}
      <div className="px-5 text-center">
        <SemiBoldText size="lg">팝업 설명</SemiBoldText>
      </div>
      <div className="w-full h-2 bg-main mt-2"></div>
      <div className="px-5">
        <DescriptionTab />
      </div>

      <BottomButtonContainer>
        <StandardButton
          onClick={() => {
            console.log('웨이팅');
          }}
          disabled={false}
          color="primary"
        >
          웨이팅 하기
        </StandardButton>
      </BottomButtonContainer>
    </div>
  );
}
