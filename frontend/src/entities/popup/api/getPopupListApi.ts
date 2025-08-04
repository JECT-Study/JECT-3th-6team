import { APIBuilder, logError } from '@/shared/lib';
import {
  PopupItemType,
  RawPopupItemType,
} from '@/entities/popup/types/PopupListItem';
import { tagPopupItem } from '@/entities/popup/lib/tagPopupItem';
import { ApiError } from 'next/dist/server/api-utils';

interface PopupListResponse {
  content: RawPopupItemType[];
  hasNext: boolean;
  lastPopupId: number;
}

interface TaggedPopupListResponse {
  hasNext: boolean;
  lastPopupId: number;
  content: PopupItemType[];
}

export default async function getPopupListApi(): Promise<TaggedPopupListResponse> {
  try {
    const response = await APIBuilder.get('/popups')
      .timeout(5000)
      .setCache('force-cache')
      .build()
      .call<PopupListResponse>();
    const { content, hasNext, lastPopupId } = response.data;

    // 기본 팝업 리스트 아이템용 태그 부착
    const TaggedData: PopupItemType[] = content.map(item =>
      tagPopupItem(item, 'DEFAULT')
    );

    return {
      content: TaggedData,
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
