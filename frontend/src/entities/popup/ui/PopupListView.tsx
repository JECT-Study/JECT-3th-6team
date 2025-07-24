import { PopupItemType } from '@/entities/popup/types/PopupListItem';
import { BadgedPopupCard } from '@/entities/popup';

interface PopupListViewProps {
  data: PopupItemType[];
}

export default function PopupListView({ data }: PopupListViewProps) {
  // TODO : 데이터가 없을 경우 처리

  return (
    <div className={'flex flex-col gap-y-3 mt-4 pb-[90px]'}>
      {data.map((popup, index) => (
        <BadgedPopupCard {...popup} key={index} />
      ))}
    </div>
  );
}
