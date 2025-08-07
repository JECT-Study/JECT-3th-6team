import { PopupItemType } from '@/entities/popup/types/PopupListItem';
import { BadgedPopupCard } from '@/entities/popup';
import EmptyPopupListFallback from '@/entities/popup/ui/EmptyPopupListFallback';

interface PopupListViewProps {
  data: PopupItemType[];
}

export default function PopupListView({ data }: PopupListViewProps) {
  return (
    <div className={'flex flex-col gap-y-3 mt-4 pb-[90px]'}>
      {data.length === 0 ? (
        <EmptyPopupListFallback type={'HISTORY'} />
      ) : (
        data.map((popup, index) => <BadgedPopupCard key={index} {...popup} />)
      )}
    </div>
  );
}
