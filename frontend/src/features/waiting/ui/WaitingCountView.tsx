import Image from 'next/image';
import LogoImage from '/public/images/spotit-logo.png';
import { PopupHistoryListItemType } from '@/entities/popup/types/PopupListItem';

export default function WaitingCountView({
  data,
}: {
  data: PopupHistoryListItemType;
}) {
  return (
    <div className={'w-full h-[70vh]  flex items-center justify-center '}>
      <div className={'flex flex-col items-center justify-center gap-y-4'}>
        <Image src={LogoImage} alt="spotit-logo" width={64} height={56} />
        <div className={'flex flex-col items-center gap-y-[10px]'}>
          <span className={'font-medium text-gray80 text-base'}>
            {data.name} 님, 앞으로
          </span>
          <span className={'text-[36px] font-semibold'}>
            <em className={'text-main not-italic'}>{data.peopleCount}팀</em>{' '}
            남았어요!
          </span>
        </div>
        <p className={'text-gray80 font-regular text-sm text-center'}>
          입장 순서가 되면 메일로 입장 알림을 보내드려요. <br />
          메일함을 꼭 확인해주세요
        </p>
      </div>
    </div>
  );
}
