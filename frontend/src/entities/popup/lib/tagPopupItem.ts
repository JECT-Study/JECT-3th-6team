// RawListItem에 타입 분기를 위한 태그 부착
import {
  PopupItemType,
  RawPopupItemType,
} from '@/entities/popup/types/PopupListItem';

export const tagPopupItem = (
  data: RawPopupItemType,
  tag: 'DEFAULT' | 'HISTORY'
): PopupItemType => {
  return {
    ...data,
    tag,
  } as PopupItemType;
};
