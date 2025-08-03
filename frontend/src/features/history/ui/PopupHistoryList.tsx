'use client';

import PopupListView from '@/entities/popup/ui/PopupListView';
import usePopupHistoryList from '@/features/history/hooks/usePopupHistoryList';
import { useIntersectionObserver } from '@/shared/hook/useIntersectionObserver';

export default function PopupHistoryList() {
  const { data, hasNextPage, isFetchingNextPage, fetchNextPage } =
    usePopupHistoryList();
  const lastElementRef = useIntersectionObserver(() => {
    if (hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  });
  return (
    <div className="flex flex-col">
      <PopupListView data={data.content} />
      {hasNextPage && <div ref={lastElementRef} className="h-4 " />}
    </div>
  );
}
