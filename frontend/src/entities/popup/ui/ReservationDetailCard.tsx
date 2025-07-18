import { PopupHistoryListItemType } from '@/entities/popup/types/PopupListItem';
import adaptHistoryToDefaultData from '@/entities/popup/lib/adapHistoryToDefaultCard';
import POPUP_MAPPER from '@/entities/popup/lib/popupMapper';
import PopupCardView from '@/entities/popup/ui/PopupCardView';
import React from 'react';

export default function ReservationDetailCard({
  data,
}: {
  data: PopupHistoryListItemType;
}) {
  const adaptedData = adaptHistoryToDefaultData(data);
  const props = POPUP_MAPPER.DEFAULT(adaptedData);
  return <PopupCardView {...props} />;
}
