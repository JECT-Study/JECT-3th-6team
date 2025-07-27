import getPopupListApi from '@/entities/popup/api/getPopupListApi';
import BadgedPopupCard from '@/entities/popup/ui/BadgedPopupCard';

export default async function FilteredPopupList() {
  const data = await getPopupListApi();

  return (
    <div className={'flex flex-col gap-y-2 px-5 pt-4.5  bg-white'}>
      <h2 className={'font-semibold text-xl text-black '}>찾은 팝업</h2>
      <div className={'flex flex-col gap-y-3 mt-4 pb-[90px]'}>
        {data.content.map((popup, index) => (
          <BadgedPopupCard {...popup} key={index} />
        ))}
      </div>
    </div>
  );
}
