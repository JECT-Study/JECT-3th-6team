import Image from 'next/image';
import { StandardButton } from '@/shared/ui';

type Props = {
  onNext: () => void;
  onStartService: () => void;
};

export default function LandingStart(props: Props) {
  const { onNext, onStartService } = props;

  return (
    <div className={'grid grid-cols-[1fr_1fr] gap-x-9'}>
      <Image
        src={'/images/landing/LANDING_START.svg'}
        width={600}
        height={720}
        alt={'service-landing-image'}
      />
      <div
        className={
          'relative flex flex-col gap-y-[62px] justify-center items-center'
        }
      >
        <Image
          src={'/images/landing/LANDING_LOGO_SMALL.svg'}
          width={106}
          height={36}
          alt={'small-logo'}
          className={'absolute top-0 left-0 '}
        />
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
        <div
          className={
            'flex flex-col gap-y-[16px] justify-center items-center w-[334px]'
          }
        >
          <StandardButton
            className={'w-[344px] relative group overflow-hidden'}
            onClick={onNext}
            disabled={false}
            color={'primary'}
          >
            <span className="relative z-20">둘러 보기</span>
            <span
              className={
                'absolute top-0 right-0 h-full w-0 bg-[#FE6731] rounded-lg transition-all duration-300 ease-in-out group-hover:left-0 group-hover:w-full z-10'
              }
            />
          </StandardButton>
          <StandardButton
            onClick={onStartService}
            disabled={false}
            color={'white'}
            className={'text-main'}
          >
            지금 바로 시작하기
          </StandardButton>
        </div>
      </div>
    </div>
  );
}
