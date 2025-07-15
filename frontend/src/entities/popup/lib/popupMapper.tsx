import {
  PopupCardViewProps,
  PopupHistoryListItemType,
  PopupItemType,
  PopupListItemType,
} from '@/entities/popup/types/PopupListItem';
import { PopupBadge } from '@/entities/popup/ui/PopupBadge';
import { dateToPeriodString, periodStringToDate } from './dateToPeriodString';

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
    period: periodStr,
    linkTo: `/detail/${data.id}`,
    Badge: renderedBadge,
    //rating: data.rating,
  };
};

const mapHistoryItemToViewProps = (
  data: PopupHistoryListItemType
): PopupCardViewProps => {
  const renderedBadge = <PopupBadge data={data} />;

  //  "2025-06-01 ~ 2025-06-25" 을 '6월 1일 ~ 6월 25일' 로 변환
  const { startDate, endDate } = periodStringToDate(data.popup.period);
  const renderedPeriod = dateToPeriodString(startDate, endDate);

  return {
    popupId: data.popup.popupId,
    popupName: data.popup.popupName,
    popupImageUrl: data.popup.popupImageUrl,
    location: data.popup.location.addressName,
    period: renderedPeriod,
    hasRightBar: data.status === 'WAITING',
    linkTo:
      data.status === 'WAITING' ? '/waiting' : `/detail/${data.popup.popupId}`,
    Badge: renderedBadge,
    // rating: data.rating,
  };
};

const POPUP_MAPPER: PopupItemMapperMapSafe = {
  DEFAULT: mapPopupListItemToViewProps,
  HISTORY: mapHistoryItemToViewProps,
};

export default POPUP_MAPPER;
