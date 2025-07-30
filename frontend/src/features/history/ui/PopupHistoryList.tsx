import PopupListView from '@/entities/popup/ui/PopupListView';
import getPopupHistoryListApi from '@/features/history/api/getPopupHistoryListApi';

export default async function PopupHistoryList() {
  const { content } = await getPopupHistoryListApi({});

  return <PopupListView data={content} />;
}
