import popupList from '@/entities/popup/api/data';
import PopupListView from '@/entities/popup/ui/PopupListView';

export default function FilteredPopupList() {
  return (
    <div className={'flex flex-col gap-y-2 px-5 pt-4.5  bg-white'}>
      <h2 className={'font-semibold text-xl text-black '}>찾은 팝업</h2>
      <PopupListView data={popupList} />
    </div>
  );
}
