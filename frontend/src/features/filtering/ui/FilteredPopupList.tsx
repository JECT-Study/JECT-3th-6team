'use client';

import useFilteredPopupList from '@/entities/popup/hook/useFilteredPopuplist';
import FilteredPopupListView from './FilteredPopupListView';
import PopupCardListSuspenseFallback from '@/entities/popup/ui/PopupCardListSuspenseFallback';
import FilteredPopupListErrorFallback from '@/features/filtering/ui/FilteredPopupListErrorFallback';

export default function FilteredPopupList() {
  const { data, isLoading, isError, refetch } = useFilteredPopupList();

  if (isError) return <FilteredPopupListErrorFallback onRetry={refetch} />;
  if (isLoading) return <PopupCardListSuspenseFallback />;
  if (!data) return null;

  return <FilteredPopupListView data={data.content} />;
}
