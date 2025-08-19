'use client';

import { useParams, useRouter } from 'next/navigation';
import { useQuery } from '@tanstack/react-query';

import { getPopupDetailApi } from '@/entities/popup/detail/api/api';

import { Badge } from '@/shared/ui/badge/Badge';
import { Tag } from '@/shared/ui/tag/Tag';
import { BottomButtonContainer, CircleMap } from '@/shared/ui';
import PageHeader from '@/shared/ui/header/PageHeader';
import StandardButton from '@/shared/ui/button/StandardButton';
import { MediumText } from '@/shared/ui/text/MediumText';
import { SemiBoldText } from '@/shared/ui/text/SemiBoldText';

import { DescriptionTab } from '@/features/detail/ui/DescriptionTab';
import { ImageCarousel } from '@/features/detail/ui/ImageCarousel';

import IconClock from '@/assets/icons/Normal/Icon_Clock.svg';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';

import LoadingFallback from '@/shared/ui/loading/LoadingFallback';
import QueryErrorFallback from '@/shared/ui/error/QueryErrorFallback';
import { requestCameraAccessWithDeepLink } from '@/shared/lib/requestCameraAccess';

export default function ProductDetail() {
  const router = useRouter();
  const params = useParams();
  const popupId = Number(params.popupId);

  const {
    data: popupDetailData,
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: ['popupDetail', popupId],
    queryFn: () => getPopupDetailApi(popupId),
  });

  if (isLoading) {
    return <LoadingFallback />;
  }

  if (isError) {
    return <QueryErrorFallback error={error} />;
  }

  if (!popupDetailData) {
    return (
      <QueryErrorFallback
        error={new Error('팝업 데이터를 찾을 수 없습니다.')}
      />
    );
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

  const handleWaitingClick = async () => {
    if (status === 'NONE') {
      try {
        await requestCameraAccessWithDeepLink((qrData: string) => {
          console.log('QR 코드 스캔 성공:', qrData);

          // QR 코드 데이터 처리 로직
          if (qrData.includes('spotit.co.kr') || qrData.includes('waiting')) {
            // 웨이팅 관련 QR 코드인 경우
            alert('웨이팅이 등록되었습니다!');
            // 여기에 실제 웨이팅 API 호출 로직 추가
            router.push('/waiting/123'); // 예시 - 실제 웨이팅 ID로 교체 필요
          } else {
            alert('올바른 웨이팅 QR 코드가 아닙니다.');
          }
        });

        // 모바일에서는 카메라 앱이 실행되고, 사용자가 QR을 스캔하면
        // QR 코드에 포함된 링크를 통해 자동으로 웨이팅 페이지로 이동됩니다.
        console.log('기본 카메라로 QR 코드를 스캔해주세요.');
      } catch (error) {
        console.error('QR 스캔 실패:', error);

        // 에러 메시지가 단순히 사용자 취소인 경우 알림을 표시하지 않음
        if (error instanceof Error && !error.message.includes('취소')) {
          alert(
            'QR 코드 스캔 중 문제가 발생했습니다. 기본 카메라 앱을 직접 열어서 QR 코드를 스캔해보세요.'
          );
        }
      }
    }
  };

  return (
    <div className="pb-36">
      <PageHeader title="상세 정보" />
      {/* Image Carousel */}
      <ImageCarousel images={thumbnails} />

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
          {searchTags.category.map((category, index) => (
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
        <div className="flex items-center gap-2 mt-2.5">
          <IconMap
            width={22}
            height={22}
            fill={'var(--gray-80)'}
            stroke={'var(--gray-80)'}
          />
          <MediumText color="color-black">위치</MediumText>
          <MediumText color="text-gray60">|</MediumText>
          <MediumText color="color-black">{location.addressName}</MediumText>
        </div>
        {/* Map */}
        <div className="mt-6.5">
          <CircleMap
            center={{ lat: location.latitude, lng: location.longitude }}
            maxLevel={6}
            minLevel={6}
            onClick={handleClickMap}
          />
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
