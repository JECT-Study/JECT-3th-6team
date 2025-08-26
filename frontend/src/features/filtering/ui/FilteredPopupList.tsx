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
      <FilteredPopupListView
        data={data.content}
        lastElementRef={lastElementRef}
      />
    </div>
  );
}
