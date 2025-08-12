import Image from 'next/image';
import { StandardButton } from '@/shared/ui';

export default function LandingEnd({
  onStartService,
}: {
  onStartService: () => void;
}) {
  return (
    <div className={'relative'}>
      <Image
        src={'/images/landing/LANDING_LOGO_SMALL.svg'}
        width={106}
        height={36}
        alt={'small-logo'}
        className={'fixed top-10 left-10 '}
      />
      <Image
        src={'/images/landing/LANDING_END_BG.png'}
        width={1280}
        height={606}
        alt={'landing end background'}
        className={'object-cover w-[100vw] h-[60vh]'}
      />
      <div
        className={
          'w-[554px] h-[496px] bg-white/70 rounded-[30px] absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 flex flex-col gap-y-[62px] justify-center items-center'
        }
      >
        <div
          className={'flex flex-col gap-y-[25px] justify-center items-center'}
        >
          <Image
            src={'/images/landing/LANDING_LOGO_BIG.svg'}
            width={130}
            height={170}
            alt={'logo-big'}
          />
          <p className={'text-[20px] text-gray60 font-semibold'}>
            지금, 이 순간의 핫플을 스팟잇! &quot; Spot it! &quot;
          </p>
        </div>
        <StandardButton
          className={'w-[344px] relative group overflow-hidden'}
          color={'primary'}
          disabled={false}
          onClick={onStartService}
        >
          <span className="relative z-20">지금 바로 시작하기</span>
          <span
            className={
              'absolute top-0 right-0 h-full w-0 bg-[#FE6731] rounded-lg transition-all duration-300 ease-in-out group-hover:left-0 group-hover:w-full z-10'
            }
          />
        </StandardButton>
      </div>
    </div>
  );
}
