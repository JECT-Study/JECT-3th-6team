import popupList from '@/entities/popup/constants/data';
import BadgedPopupCard from '@/entities/popup/ui/BadgedPopupCard';

export default function FilteredPopupList() {
  return (
    <div className={'flex flex-col gap-y-2 px-5 pt-4.5  bg-white'}>
      <h2 className={'font-semibold text-xl text-black '}>찾은 팝업</h2>
      <div className={'flex flex-col gap-y-3 mt-4 pb-[90px]'}>
        {popupList.map((popup, index) => (
          <BadgedPopupCard {...popup} key={index} />
        ))}
      </div>
    </div>
  );
}
