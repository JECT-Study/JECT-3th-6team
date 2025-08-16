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
      {/*좌측 영역*/}

      <div
        className={
          'w-[600px] h-[722px] relative ' +
          'bg-[#F4F4F4] rounded-[30px] flex flex-col justify-center items-center gap-y-[10px]'
        }
      >
        <p
          className={
            'w-[500px] text-[22px] text-[#4A4A4A] font-medium text-center mt-[20px] absolute top-[87px] left-1/2 -translate-x-1/2 select-none'
          }
        >
          팝업스토어 정보를 모아보고, 현장예약과 실시간 <br />
          웨이팅까지 가능한 통합 플랫폼
          <em className={'text-main not-italic'}> ‘Spot it!’ </em>을 알아볼까요?
        </p>

        <Image
          src={'/images/landing/LANDING_START_PHONE.png'}
          width={416}
          height={477}
          alt={'service-landing-image'}
          className={
            'object-fill w-[450px] absolute top-[203px] left-1/2 -translate-x-1/2'
          }
          priority
          fetchPriority="high"
        />
      </div>

      {/*우측 영역*/}
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
