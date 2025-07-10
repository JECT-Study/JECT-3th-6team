import {
  PopupCardViewProps,
  PopupHistoryListItemType,
  PopupItemType,
  PopupListItemType,
} from '@/entities/popup/types/PopupListItem';
import { PopupBadge } from '@/entities/popup/ui/PopupBadge';
import { dateToPeriodString } from './dateToPeriodString';

type PopupItemMapperMap = {
  DEFAULT: (data: PopupListItemType) => PopupCardViewProps;
  HISTORY: (data: PopupHistoryListItemType) => PopupCardViewProps;
};

type PopupItemMapperMapSafe = {
  [K in keyof PopupItemMapperMap]: (
    data: Parameters<PopupItemMapperMap[K]>[0] extends PopupItemType
      ? Parameters<PopupItemMapperMap[K]>[0]
      : never
  ) => PopupCardViewProps;
};

const mapPopupListItemToViewProps = (
  data: PopupListItemType
): PopupCardViewProps => {
  const renderedBadge = <PopupBadge data={data} />;
  const periodStr = dateToPeriodString(
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
};

const mapHistoryItemToViewProps = (
  data: PopupHistoryListItemType
): PopupCardViewProps => {
  const renderedBadge = <PopupBadge data={data} />;

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

const POPUP_MAPPER: PopupItemMapperMapSafe = {
  DEFAULT: mapPopupListItemToViewProps,
  HISTORY: mapHistoryItemToViewProps,
};

export default POPUP_MAPPER;
