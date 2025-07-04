import Image from 'next/image';
import { TabsContent } from '@/components/ui/tabs';
import { MediumText } from '@/shared/ui/text/MediumText';
import { ImageCarousel } from './ImageCarousel';

export function DescriptionTab() {
  const images = [
    '/images/sunglass.jpg',
    '/images/sunglass.jpg',
    '/images/sunglass.jpg',
  ];
  return (
    <TabsContent value="description" className="px-5 py-6">
      <div>
        {/* 일정 */}
        <div className="w-full h-13 bg-sub rounded-[10px] flex items-center px-5 gap-4">
          <div className="flex items-center gap-2">
            <Image
              src="/icons/Normal/Icon_Clock.svg"
              alt="clock"
              width={19}
              height={17}
              className="w-5 h-4"
            />
            <span>일정</span>
          </div>
          <div>|</div>
          <div className="flex-1 text-center">25.06.01(월) ~ 25.06.30(수)</div>
        </div>
        {/* 브랜드 정보 */}
        <MediumText className="mt-5">브랜드 정보</MediumText>
        <ImageCarousel images={images} className="rounded-2xl mt-5" />
        <div className="w-full h-13 bg-white rounded-[10px] flex items-center px-5 shadow-[0px_2px_10px_0px_rgba(0,0,0,0.05)] mt-5">
          <div className="flex items-center justify-between w-full">
            <div>
              <span>브랜드 SNS로 연결</span>
            </div>
            <div className="flex items-center gap-2">
              <Image
                src="/icons/Color/Icon_Instagram.svg"
                alt="clock"
                width={26}
                height={26}
                className="w-6.5 h-6.5"
              />
              <Image
                src="/icons/Color/Icon_Facebook.svg"
                alt="clock"
                width={26}
                height={26}
                className="w-6.5 h-6.5"
              />
              <Image
                src="/icons/Color/Icon_NaverBlog.svg"
                alt="clock"
                width={26}
                height={26}
                className="w-6.5 h-6.5"
              />
            </div>
          </div>
        </div>
        {/* 상세 정보 */}
        <div className="mt-5 py-5">
          <MediumText>상세 정보</MediumText>
        </div>
        <div>
          <h4 className="font-medium">운영 시간</h4>
          <p className="text-sm text-gray-600 mt-2">⏰ 월-목 08:00 - 22:00</p>
          <p className="text-sm text-gray-600 pl-4">금-일 10:00 - 22:00</p>
          <p className="text-sm text-gray-600">❗️휴무 7/31(금) 10:00 - 22:00</p>
        </div>
        <div className="mt-4.5">
          <h4 className="font-medium ">콘텐츠</h4>
          <p className="text-sm text-gray-600 mt-2">✅ 라커디움파크</p>
          <p className="text-sm text-gray-600">
            KBO, 팀코리아 등 야구팬을 위한 굿즈 아이템을 만나보세요
          </p>
          <p className="text-sm text-gray-600">✅ 라커디움파크</p>
          <p className="text-sm text-gray-600">
            ①응원하는 KBO팀 투표시 야구공 증정 이벤트 (매장내 비치된
            투표보드지에 응원하는 KBO팀 투표, 매장 인스타그램 팔로우시 야구공
            증정 (1인1회 한정)) ②스포츠일러스트레이티드 x 몽베스트 콜라보레이션
            상품 구매시 스페셜기프트 및 전용패키징 증정 (한정수량)
          </p>
        </div>
        <div className="border-t border-gray40 mt-5 -mx-5"></div>
      </div>
    </TabsContent>
  );
}
