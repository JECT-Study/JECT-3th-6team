import React from 'react';
import { Badge } from '@/shared/ui/badge/Badge';
import IconClock from '@/assets/icons/Normal/Icon_Clock.svg';
import { PopupItemType } from '@/entities/popup/types/PopupListItem';

type Props = {
  data: PopupItemType;
};

export const PopupBadge = ({ data }: Props): React.ReactElement => {
  const BADGE_POSITION_STYLE = 'absolute top-[12px] left-[8px]';
  if (data.tag === 'DEFAULT') {
    return (
      <Badge className={BADGE_POSITION_STYLE}>
        <IconClock width={12} height={12} fill={'var(--color-white)'} />
        {data.dDay}일 남음
      </Badge>
    );
  }

  if (data.status === 'VISITED') {
    return (
      <Badge variant="gray" className={BADGE_POSITION_STYLE}>
        방문완료
      </Badge>
    );
  }

  if (data.status === 'WAITING') {
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
