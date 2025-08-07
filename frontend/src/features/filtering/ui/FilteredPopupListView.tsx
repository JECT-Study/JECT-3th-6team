import { PopupListItemType } from '@/entities/popup/types/PopupListItem';
import EmptyPopupListFallback from '@/entities/popup/ui/EmptyPopupListFallback';
import { BadgedPopupCard } from '@/entities/popup';

interface FilteredPopupListViewProps {
  data: PopupListItemType[]; // 타입에 따라 수정
}

export default function FilteredPopupListView({
  data,
}: FilteredPopupListViewProps) {
  return (
    <div className="flex flex-col gap-y-2 px-5 pt-4.5 bg-white">
      <h2 className="font-semibold text-xl text-black">찾은 팝업</h2>
      <div className="flex flex-col gap-y-3 mt-4 pb-[90px]">
        {data.length === 0 ? (
          <EmptyPopupListFallback type={'DEFAULT'} />
        ) : (
          data.map((popup, index) => <BadgedPopupCard key={index} {...popup} />)
        )}
      </div>
    </div>
  );
}
