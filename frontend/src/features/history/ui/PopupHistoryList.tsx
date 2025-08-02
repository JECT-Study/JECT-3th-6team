'use client';

import PopupListView from '@/entities/popup/ui/PopupListView';
import usePopupHistoryList from '@/features/history/hooks/usePopupHistoryList';

export default function PopupHistoryList() {
  const { data } = usePopupHistoryList({});

  return <PopupListView data={data.content} />;
}
