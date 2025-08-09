import { PopupItemType } from '@/entities/popup/types/PopupListItem';
import { BadgedPopupCard } from '@/entities/popup';
import EmptyPopupListFallback from '@/entities/popup/ui/EmptyPopupListFallback';

interface PopupListViewProps {
  data: PopupItemType[];
  lastElementRef: React.RefObject<HTMLDivElement | null>;
}

export default function PopupListView({
  data,
  lastElementRef,
}: PopupListViewProps) {
  if (data.length === 0) return <EmptyPopupListFallback type={'HISTORY'} />;
  return (
    <div className={'flex flex-col gap-y-3 mt-4 pb-[90px]'}>
      {data.map((popup, index) => (
        <BadgedPopupCard key={index} {...popup} />
      ))}
      <div ref={lastElementRef} className="h-4 " />
    </div>
  );
}
