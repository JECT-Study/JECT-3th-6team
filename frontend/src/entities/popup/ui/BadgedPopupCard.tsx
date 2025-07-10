import PopupCardView from '@/entities/popup/ui/PopupCardView';
import React from 'react';
import {
  PopupHistoryListItemType,
  PopupItemType,
  PopupListItemType,
} from '@/entities/popup/types/PopupListItem';
import POPUP_MAPPER from '@/entities/popup/lib/popupMapper';

function getPopupCardViewProps(
  type: keyof typeof POPUP_MAPPER,
  data: PopupItemType
) {
  switch (type) {
    case 'DEFAULT':
      return POPUP_MAPPER.DEFAULT(data as PopupListItemType);
    case 'HISTORY':
      return POPUP_MAPPER.HISTORY(data as PopupHistoryListItemType);
  }
}

export default function BadgedPopupCard(data: PopupItemType) {
  const prop = getPopupCardViewProps(data.tag, data);
  return <PopupCardView {...prop} />;
}
