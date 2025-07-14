import BadgedPopupCard from '@/entities/popup/ui/BadgedPopupCard';
import { Navbar } from '@/widgets';
import BellIcon from '@/assets/icons/Normal/Icon_Bell.svg';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import IconCalendar from '@/assets/icons/Normal/Icon_Calendar.svg';
import { SelectButton } from '@/shared/ui';
import popupList from '@/entities/popup/constants/data';

export default function Home() {
  return (
    <div className={'w-full bg-main pt-7'}>
      {/*필터링 컨테이너*/}
      <div className={'flex flex-col px-5 gap-y-4 mb-4'}>
        <div className={'flex items-center justify-between'}>
          <span className={'font-gangwon text-[17px] text-white font-regular'}>
            Spot it
          </span>
          <BellIcon fill={'var(--color-main)'} width={28} height={28} />
        </div>
        <div className={'flex items-center gap-x-3'}>
          <SelectButton
            Icon={
              <IconCalendar
                width={22}
                height={22}
                fill={'var(--color-white)'}
                stroke={'var(--color-main)'}
              />
            }
            label={'06.11~06.20'}
          />
          <SelectButton
            Icon={<IconMap width={22} height={22} fill={'var(--color-main)'} />}
            label={'서울'}
          />
        </div>
      </div>

      {/*리스트 컨테이너*/}
      <div className={'flex flex-col px-5 pt-4.5 rounded-[20px] bg-white'}>
        <h2 className={'font-semibold text-xl text-black '}>찾은 팝업</h2>

        {/*팝업 리스트*/}
        <div className={'flex flex-col gap-y-3 mt-4 pb-[90px]'}>
          {popupList.map((popup, index) => (
            <BadgedPopupCard {...popup} key={index} />
          ))}
        </div>
      </div>
      <Navbar />
    </div>
  );
}
