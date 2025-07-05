import {
  PopupCardViewProps,
  PopupListItemType,
  VisitedPopupListItemType,
} from '@/entities/popup/types/PopupListItem';
import { dateToPeriodStr } from '@/entities/popup/lib/dateToPeriodStr';
import { Badge } from '@/app/shared/ui/badge/Badge';
import PopupCardView from '@/entities/popup/ui/PopupCardView';
import IconClock from '@/assets/icons/Normal/Icon_Clock.svg';
import React from 'react';

type PopupItemType = PopupListItemType | VisitedPopupListItemType;

// 타입 가드
const isPopupListItem = (data: PopupItemType): data is PopupListItemType => {
  return 'dDay' in data;
};

const PopupBadge = (data: PopupItemType): React.ReactElement => {
  const BADGE_POSITION_STYLE = 'absolute top-[12px] left-[8px]';
  if (isPopupListItem(data)) {
    return (
      <Badge
        icon={<IconClock width={12} height={12} fill={'var(--color-white)'} />}
        iconPosition="left"
        className={BADGE_POSITION_STYLE}
      >
        {data.dDay}일 남음
      </Badge>
    );
  }

  if (data.status === 'COMPLETED') {
    return (
      <Badge variant="gray" className={BADGE_POSITION_STYLE}>
        방문완료
      </Badge>
    );
  }

  if (data.status === 'RESERVED') {
    return (
      <Badge variant="main" className={BADGE_POSITION_STYLE}>
        예약 중
      </Badge>
    );
  }

  // fallback
  return (
    <Badge variant="gray" className={BADGE_POSITION_STYLE}>
      알 수 없음
    </Badge>
  );
};

const mapToCardProps = (data: PopupItemType): PopupCardViewProps => {
  const renderedBadge = <PopupBadge {...data} />;

  if (isPopupListItem(data)) {
    // 전체 팝업 리스트 데이터일경우
    const periodStr = dateToPeriodStr(
      new Date(data.period.startDate),
      new Date(data.period.endDate)
    );

    return {
      popupId: data.id,
      popupName: data.name,
      popupImageUrl: data.imageUrl,
      location: data.location.address_name,
      rating: data.rating,
      period: periodStr,
      linkTo: `/detail/${data.id}`,
      Badge: renderedBadge,
    };
  }

  // 방문 히스토리 리스트 데이터일 경우
  return {
    popupId: data.popupId,
    popupName: data.popupName,
    popupImageUrl: data.popupImageUrl,
    location: data.location,
    rating: data.rating,
    period: data.period,
    hasRightBar: data.status === 'RESERVED',
    linkTo: data.status === 'RESERVED' ? '/waiting' : `/detail/${data.popupId}`,
    Badge: renderedBadge,
  };
};

export default function BadgedPopupCard(data: PopupItemType) {
  return <PopupCardView {...mapToCardProps(data)} />;
}
