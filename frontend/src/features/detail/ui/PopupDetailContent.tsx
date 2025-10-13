'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';

import { Badge } from '@/shared/ui/badge/Badge';
import { Tag } from '@/shared/ui/tag/Tag';
import { BottomButtonContainer, KakaoMap } from '@/shared/ui';
import { MapMarker } from 'react-kakao-maps-sdk';
import PageHeader from '@/shared/ui/header/PageHeader';
import StandardButton from '@/shared/ui/button/StandardButton';
import { MediumText } from '@/shared/ui/text/MediumText';
import { SemiBoldText } from '@/shared/ui/text/SemiBoldText';

import { DescriptionTab } from '@/features/detail/ui/DescriptionTab';
import { ImageCarousel } from '@/features/detail/ui/ImageCarousel';
import QrScanGuideModal from '@/features/detail/ui/QrScanGuideModal';

import IconClock from '@/assets/icons/Normal/Icon_Clock.svg';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';

import { PopupDetailResponseDto } from '@/entities/popup/detail/types/type';
import TagManager from 'react-gtm-module';
import { extractLinkMetaFromButton } from '@/shared/lib';

interface PopupDetailContentProps {
  popupDetailData?: PopupDetailResponseDto;
  popupId: number;
}

export default function PopupDetailContent({
  popupDetailData,
  popupId,
}: PopupDetailContentProps) {
  const router = useRouter();
  const [isQrGuideModalOpen, setIsQrGuideModalOpen] = useState(false);

  const myLocationImageUrl = '/icons/Color/Icon_Map.svg';
  // 데이터가 없을 때 early return (Suspense에서 처리됨)
  if (!popupDetailData) {
    return null;
  }

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

  const handleClickMap = () => {
    const lat = location.latitude;
    const lng = location.longitude;
    router.push(`/detail/${popupId}/map?lat=${lat}&lng=${lng}`);
  };

  const handleWaitingClick = async (e: React.MouseEvent<HTMLButtonElement>) => {
    if (status === 'NONE') {
      // setIsQrGuideModalOpen(true);
      // try {
      //   await requestCameraAccess(() => {});
      // } catch (error) {
      //   console.error('카메라 접근 실패:', error);
      //   alert(
      //     error instanceof Error
      //       ? error.message
      //       : '카메라에 접근할 수 없습니다.'
      //   );
      // }

      const { text, url } = extractLinkMetaFromButton(e);
      // GTM
      TagManager.dataLayer({
        dataLayer: {
          event: 'popup_click',
          popup_id: String(popupId),
          link_text: text,
          link_url: url,
        },
      });

      router.push(`/reservation/onsite/${popupId}`);
    }
  };

  const handleQrGuideClose = () => {
    setIsQrGuideModalOpen(false);
  };

  return (
    <div className="pb-36">
      <PageHeader title="상세 정보" />
      {/* Image Carousel */}
      <ImageCarousel
        images={thumbnails && thumbnails.length > 0 ? thumbnails : []}
      />

      {/* Main Detail */}
      <div className="py-6 px-5">
        <div className="flex items-center justify-between">
          <Badge iconPosition="left">
            {dDay > 0 ? `${dDay}일 남음` : '종료됨'}
          </Badge>
        </div>
        {/* Title and Tags*/}
        <div className="mt-6">
          <SemiBoldText size="lg">{title}</SemiBoldText>
          <Tag>{searchTags.type}</Tag>
          {searchTags.category.map((category: string, index: number) => (
            <Tag key={index}>{category}</Tag>
          ))}
        </div>
        {/* Schedule and Location */}
        <div className="flex items-center gap-2 mt-5">
          <IconClock
            width={22}
            height={22}
            fill={'var(--gray-80)'}
            stroke={'var(--gray-80)'}
          />
          <MediumText color="color-black">일정</MediumText>
          <MediumText color="text-gray60">|</MediumText>
          <MediumText color="color-black">
            {period.startDate} ~ {period.endDate}
          </MediumText>
        </div>
        <div className="flex items-start gap-2 mt-2.5">
          <IconMap
            width={22}
            height={22}
            fill={'var(--gray-80)'}
            stroke={'var(--gray-80)'}
            className="flex-shrink-0 mt-0.5"
          />
          <div className="flex items-start gap-2 min-w-0 flex-1">
            <MediumText color="color-black" className="flex-shrink-0">
              위치
            </MediumText>
            <MediumText color="text-gray60" className="flex-shrink-0">
              |
            </MediumText>
            <MediumText color="color-black" className="break-words">
              {location.addressName}
            </MediumText>
          </div>
        </div>
        {/* Map */}
        <div className="mt-6.5">
          <KakaoMap
            center={{ lat: location.latitude, lng: location.longitude }}
            maxLevel={6}
            minLevel={6}
            onClick={handleClickMap}
            className="w-full h-30 rounded-[10px]"
          >
            <MapMarker
              position={{ lat: location.latitude, lng: location.longitude }}
              image={{
                src: myLocationImageUrl,
                size: { width: 40, height: 40 },
              }}
            />
          </KakaoMap>
        </div>
      </div>

      {/* Description Section */}
      <div className="px-5 text-center">
        {/* <SemiBoldText size="lg">팝업 설명</SemiBoldText> */}
      </div>
      <div className="w-full h-px bg-gray40 mt-7.5" />
      <div className="px-5 py-6">
        <DescriptionTab brandStory={brandStory} popupDetail={popupDetail} />
      </div>

      <BottomButtonContainer hasShadow={true}>
        <StandardButton
          onClick={handleWaitingClick}
          disabled={status === 'WAITING' || status === 'VISITED'}
          color="primary"
          className={'waiting-btn'}
        >
          {status === 'WAITING'
            ? '예약중'
            : status === 'NONE'
              ? '웨이팅하기'
              : '방문 완료'}
        </StandardButton>
      </BottomButtonContainer>

      {/* QR 스캔 안내 모달 */}
      <div>
        <QrScanGuideModal
          isOpen={isQrGuideModalOpen}
          onClose={handleQrGuideClose}
        />
      </div>
    </div>
  );
}
