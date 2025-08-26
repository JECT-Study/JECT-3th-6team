import type { PopupHistoryListItemType } from '@/entities/popup/types/PopupListItem';
import getPopupHistoryListApi from '@/features/history/api/getPopupHistoryListApi';

export async function getWaitingNumberApi(): Promise<PopupHistoryListItemType> {
  const res = await getPopupHistoryListApi({ status: 'WAITING', size: 1 });
  return res.content[0] ?? null;
}
