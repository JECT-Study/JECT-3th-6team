import { APIBuilder, logError } from '@/shared/lib';
import { ApiError } from '@/shared/type/api';
import {
  PopupHistoryListItemType,
  RawPopupHistoryListItemType,
} from '@/entities/popup/types/PopupListItem';
import { tagPopupItem } from '@/entities/popup/lib/tagPopupItem';

export interface PopupHistoryListRequest {
  size?: number;
  lastWaitingId?: number;
  status?: string;
}

export interface PopupHistoryListResponse {
  content: RawPopupHistoryListItemType[];
  lastWaitingId: number;
  hasNext: boolean;
}

export interface TaggedPopupHistoryListResponse {
  content: PopupHistoryListItemType[];
  lastWaitingId: number;
  hasNext: boolean;
}

export default async function getPopupHistoryListApi(
  request: PopupHistoryListRequest
): Promise<TaggedPopupHistoryListResponse> {
  try {
    const response = await APIBuilder.get('/me/visits')
      .params({ ...request })
      .withCredentials(true)
      .timeout(5000)
      .build()
      .call<PopupHistoryListResponse>();

    const { content, lastWaitingId, hasNext } = response.data;

    const TaggedData = content.map(item => tagPopupItem(item, 'HISTORY'));

    return {
      content: TaggedData as PopupHistoryListItemType[],
      lastWaitingId,
      hasNext,
    };
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error, '내방문/예약 데이터 불러오는 과정');
    }
    throw error;
  }
}
