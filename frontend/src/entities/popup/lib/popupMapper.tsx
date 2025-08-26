import {
  PopupCardViewProps,
  PopupHistoryListItemType,
  PopupItemType,
  PopupListItemType,
} from '@/entities/popup/types/PopupListItem';
import { PopupBadge } from '@/entities/popup/ui/PopupBadge';
import { dateToPeriodKRString, periodStringToDate } from './dateToPeriodString';
import { formatSearchTags } from '@/entities/popup/lib/formatSearchTags';

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

  const { startDate, endDate } = periodStringToDate(data.period);
  const renderedPeriod = dateToPeriodKRString(startDate, endDate);

  const { region1depthName, region2depthName } = data.location;
  const renderLocation = `${region1depthName}, ${region2depthName}`;
  const renderTag = formatSearchTags(data.searchTags);
  return {
    popupId: data.popupId,
    popupName: data.popupName,
    popupImageUrl: data.popupImageUrl,
    location: renderLocation,
    period: renderedPeriod,
    linkTo: `/detail/${data.popupId}`,
    Badge: renderedBadge,
    searchTags: renderTag,
    //rating: data.rating,
  };
};

const mapHistoryItemToViewProps = (
  data: PopupHistoryListItemType
): PopupCardViewProps => {
  const renderedBadge = <PopupBadge data={data} />;

  //  "2025-06-01 ~ 2025-06-25" 을 '6월 1일 ~ 6월 25일' 로 변환
  const { startDate, endDate } = periodStringToDate(data.popup.period);
  const renderedPeriod = dateToPeriodKRString(startDate, endDate);

  const { region1depthName, region2depthName } = data.popup.location;
  const renderLocation = `${region1depthName}, ${region2depthName}`;
  const renderTag = formatSearchTags(data.popup.searchTags);

  return {
    popupId: data.popup.popupId,
    popupName: data.popup.popupName,
    popupImageUrl: data.popup.popupImageUrl,
    location: renderLocation,
    period: renderedPeriod,
    hasRightBar: data.status === 'WAITING',
    linkTo:
      data.status === 'WAITING'
        ? `/waiting/${data.waitingId}`
        : `/detail/${data.popup.popupId}`,
    Badge: renderedBadge,
    searchTags: renderTag,
    // rating: data.rating,
  };
};

const POPUP_MAPPER: PopupItemMapperMapSafe = {
  DEFAULT: mapPopupListItemToViewProps,
  HISTORY: mapHistoryItemToViewProps,
};

export default POPUP_MAPPER;
