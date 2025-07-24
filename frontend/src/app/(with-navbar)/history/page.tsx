import PopupListView from '@/entities/popup/ui/PopupListView';
import popupList from '@/entities/popup/api/data';

export default function HistoryPage() {
  return (
    <div className={'w-full h-full flex flex-col px-[20px]'}>
      <h1 className={'font-semibold text-2xl my-[26px]'}>내 방문</h1>
      <PopupListView data={popupList} />
    </div>
  );
}
