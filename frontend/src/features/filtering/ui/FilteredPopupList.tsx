'use client';

import useFilteredPopupList from '@/entities/popup/hook/useFilteredPopuplist';
import FilteredPopupListView from './FilteredPopupListView';

export default function FilteredPopupList() {
  const { data } = useFilteredPopupList();

  if (!data) return null;

  return <FilteredPopupListView data={data.content} />;
}
