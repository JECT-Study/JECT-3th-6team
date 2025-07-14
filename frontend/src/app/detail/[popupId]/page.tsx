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

export default function ProductDetail() {
  const images = [
    '/images/sunglass.jpg',
    '/images/sunglass.jpg',
    '/images/sunglass.jpg',
  ];

  return (
    <div>
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
        {/* Location */}
        <div className="flex items-center justify-between mt-2.5">
          <div className="flex items-center">
            <Image
              src="/icons/Normal/Icon_map.svg"
              alt="map"
              width={22}
              height={22}
              className="w-5.5 h-5.5 mr-1"
            />
            <MediumText>서울, 용산구 한남동 61-2</MediumText>
          </div>
          {/* 없어도 무관한 화살표 */}
          {/* <ChevronRight className="w-4 h-4 text-gray-400" /> */}
        </div>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="description" className="w-full gap-0">
        <TabsList className="grid w-full grid-cols-2 bg-transparent border-b-2 border-sub rounded-none h-auto p-0">
          <TabsTrigger
            value="description"
            className="data-[state=active]:bg-transparent data-[state=active]:shadow-none border-0 data-[state=active]:border-0 data-[state=active]:border-b-4 data-[state=active]:border-main data-[state=active]:text-black data-[state=active]:-mb-1 rounded-none pb-3 py-4 text-base font-medium"
          >
            팝업 설명
          </TabsTrigger>
          <TabsTrigger
            value="reviews"
            className="data-[state=active]:bg-transparent data-[state=active]:shadow-none border-0 data-[state=active]:border-0 data-[state=active]:border-b-4 data-[state=active]:border-main data-[state=active]:text-black data-[state=active]:-mb-1 rounded-none pb-3 py-4 text-base font-medium"
          >
            리뷰
          </TabsTrigger>
        </TabsList>
        <DescriptionTab />
        <ReviewTab />
      </Tabs>
    </div>
  );
}
