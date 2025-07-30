import { APIBuilder, logError } from '@/shared/lib';
import {
  PopupListItemType,
  RawPopupListItemType,
} from '@/entities/popup/types/PopupListItem';
import { tagPopupItem } from '@/entities/popup/lib/tagPopupItem';
import { ApiError } from 'next/dist/server/api-utils';

export interface PopupListRequest {
  popupId?: number;
  type?: string[]; // type=체험형&type=전시형
  category?: string[]; // category=패션&category=예술
  startDate?: string; // startDate=2025-07-01
  endDate?: string;
  region1DepthName?: string;
  lastPopupId?: number;
  size?: number;
}

interface PopupListResponse {
  content: RawPopupListItemType[];
  hasNext: boolean;
  lastPopupId: number;
}

interface TaggedPopupListResponse {
  hasNext: boolean;
  lastPopupId: number;
  content: PopupListItemType[];
}

export default async function getPopupListApi(
  request: PopupListRequest
): Promise<TaggedPopupListResponse> {
  try {
    const response = await (
      await APIBuilder.get('/popups')
        .timeout(5000)
        .params({ ...request })
        .buildAsync()
    ).call<PopupListResponse>();
    const { content, hasNext, lastPopupId } = response.data;

    // 기본 팝업 리스트 아이템용 태그 부착
    const TaggedData = content.map(item => tagPopupItem(item, 'DEFAULT'));

    return {
      content: TaggedData as PopupListItemType[],
      lastPopupId: lastPopupId,
      hasNext: hasNext,
    };
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error, '팝업리스트 불러오는 과정');
    }
    throw error;
  }
}
