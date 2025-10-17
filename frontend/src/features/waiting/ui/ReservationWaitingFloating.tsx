'use client';

import useWaitingNumber from '@/features/waiting/hook/useWaitingNumber';
import { useEffect } from 'react';
import { useUserStore } from '@/entities/user/lib/useUserStore';
import LogoImage from '/public/images/spotit-logo.png';
import Image from 'next/image';
import IconArrow from '@/assets/icons/Normal/Icon_Bracket_Right.svg';
import { useRouter } from 'next/navigation';
import { logger } from '@/shared/lib';

export default function ReservationWaitingFloating() {
  const { isLoggedIn } = useUserStore(state => state.userState);
  const { data, isError, error } = useWaitingNumber();
  const router = useRouter();

  useEffect(() => {
    if (data?.status === 'VISITED') {
    }
  }, [data?.status]);
  if (!isLoggedIn) return null;
  if (isError)
    logger.error(
      'error :',
      '대기 순번을 불러오는데 실패했습니다.',
      error.message
    );
  if (data?.status !== 'WAITING') return null;

  return (
    // 대기 순번 페이지로 이동
    <div
      className={
        'w-full max-w-[430px] min-w-[320px] py-[34px] fixed bottom-16 z-50 flex justify-center px-[20px] '
      }
    >
      <div
        onClick={() => {
          router.push(`/waiting/${data.waitingId}`);
        }}
        className="w-full h-[72px] rounded-full  bg-white shadow-card flex items-center justify-between px-[16px] cursor-pointer hover:bg-sub2 transition-colors duration-300"
      >
        {/*이미지와 팝업이름*/}
        <div className={'flex items-center gap-x-[12px]'}>
          <div
            className={
              'w-[48px] h-[48px] overflow-hidden rounded-full flex items-center justify-center border border-sub'
            }
          >
            <Image src={LogoImage} alt="spotit-logo" width={25} height={25} />
          </div>
          <div className={'flex flex-col'}>
            <p className={'font-medium text-[16px]'}>{data.popup.popupName}</p>
            <span className={'font-regular text-gray80 text-[14px]'}>
              예상 대기 시간 :
              <strong className={'text-main'}>
                {data.waitingNumber === 0 ? 0 : data.expectedWaitingTimeMinutes}
                분
              </strong>
            </span>
          </div>
        </div>

        {/*대기순번과 화살표*/}
        <div className={'flex items-center gap-x-[14px]'}>
          <span className={'text-[24px] font-semibold text-main'}>
            {data.waitingNumber === 0 ? '지금 입장' : `${data.waitingNumber}팀`}
          </span>
          <IconArrow width={22} height={22} fill={'var(--color-gray80)'} />
        </div>
      </div>
    </div>
  );
}
