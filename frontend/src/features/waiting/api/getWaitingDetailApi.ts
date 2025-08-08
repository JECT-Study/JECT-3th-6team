import { APIBuilder, logError } from '@/shared/lib';
import { ApiError } from '@/shared/type/api';
import {
  PopupHistoryListItemType,
  RawPopupHistoryListItemType,
} from '@/entities/popup/types/PopupListItem';
import { tagPopupItem } from '@/entities/popup/lib/tagPopupItem';

interface WaitingDetailResponse {
  content: RawPopupHistoryListItemType[];
}

export async function getWaitingDetailApi(
  waitingId: number
): Promise<PopupHistoryListItemType> {
  try {
    const response = await APIBuilder.get('/me/visits')
      .params({
        size: 1,
        waitingId,
      })
      .withCredentials(true)
      .timeout(5000)
      .build()
      .call<WaitingDetailResponse>();

    const { content } = response.data;
    const taggedData = tagPopupItem(content[0], 'HISTORY');
    return taggedData as PopupHistoryListItemType;
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error, `waitingId=${waitingId} 단건 조회 실패`);
    }
    throw error;
  }
}
