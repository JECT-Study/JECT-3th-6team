import Image from 'next/image';
import Link from 'next/link';

import { ImageCarousel } from './ImageCarousel';
import { MediumText } from '@/shared/ui/text/MediumText';
import { RegularText } from '@/shared/ui/text/RegularText';

interface BrandStory {
  imageUrls: string[];
  sns: Array<{
    icon: string;
    url: string;
  }>;
}

interface PopupDetail {
  dayOfWeeks: Array<{
    dayOfWeek: string;
    value: string;
  }>;
  descriptions: string[];
}

interface DescriptionTabProps {
  brandStory: BrandStory;
  popupDetail: PopupDetail;
}

const convertDayOfWeekToKorean = (dayOfWeek: string): string => {
  const dayOfWeekMap: Record<string, string> = {
    MONDAY: '월',
    TUESDAY: '화',
    WEDNESDAY: '수',
    THURSDAY: '목',
    FRIDAY: '금',
    SATURDAY: '토',
    SUNDAY: '일',
  };

  return dayOfWeekMap[dayOfWeek];
};

export function DescriptionTab({
  brandStory,
  popupDetail,
}: DescriptionTabProps) {
  return (
    // <TabsContent value="description" className="px-5 py-6">
    <div className="px-5 py-6">
      {/* 브랜드 정보 */}
      <MediumText className="mt-5">브랜드 정보</MediumText>
      <ImageCarousel
        images={brandStory.imageUrls}
        className="rounded-2xl mt-5"
      />
      <div className="w-full h-13 bg-white rounded-[10px] flex items-center px-5 shadow-[0px_2px_10px_0px_rgba(0,0,0,0.05)] mt-5">
        <div className="flex items-center justify-between w-full">
          <div>
            <span>브랜드 SNS로 연결</span>
          </div>
          <div className="flex items-center gap-2">
            {brandStory.sns.map((snsItem, index) => (
              <Link
                key={index}
                href={snsItem.url}
                target="_blank"
                className="cursor-pointer"
              >
                <Image
                  src={`/icons/Color/Icon_NaverBlog.svg`}
                  alt="sns"
                  width={26}
                  height={26}
                  className="w-6.5 h-6.5"
                />
              </Link>
            ))}
          </div>
        </div>
      </div>
      {/* 상세 정보 */}
      <div className="mt-5 py-5">
        <MediumText className="text-black">상세 정보</MediumText>
      </div>
      <div>
        <RegularText className="text-[15px] text-black">운영 시간</RegularText>
        {popupDetail.dayOfWeeks.map((dayInfo, index) => {
          const koreanDay = convertDayOfWeekToKorean(dayInfo.dayOfWeek);

          return (
            <p
              key={index}
              className={`text-sm text-black ${index === 0 ? '' : 'pl-4'}`}
            >
              {index === 0 ? '⏰ ' : ''}
              {koreanDay} {dayInfo.value}
            </p>
          );
        })}
      </div>
      <div className="mt-4.5">
        <RegularText className="text-[15px] text-black">콘텐츠</RegularText>
        {popupDetail.descriptions.map((description, index) => (
          <p key={index} className="text-sm text-black">
            {description}
          </p>
        ))}
      </div>
      <div className="border-t border-gray40 mt-5 -mx-5"></div>
    </div>
    // </TabsContent>
  );
}
