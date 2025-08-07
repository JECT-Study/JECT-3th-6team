'use client';

import useFilteredPopupList from '@/entities/popup/hook/useFilteredPopuplist';
import FilteredPopupListView from './FilteredPopupListView';
import { useIntersectionObserver } from '@/shared/hook/useIntersectionObserver';

export default function FilteredPopupList() {
  const { data, hasNextPage, isFetchingNextPage, fetchNextPage } =
    useFilteredPopupList();
  const lastElementRef = useIntersectionObserver(() => {
    if (hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  });
  if (!data) return null;

  return (
    <div className="flex flex-col">
      <FilteredPopupListView data={data.content} />
      {/* 관찰용 엘리먼트 */}
      {hasNextPage && (
        <div ref={lastElementRef} className="h-4 bg-yellow-200" />
      )}
    </div>
  );
}
