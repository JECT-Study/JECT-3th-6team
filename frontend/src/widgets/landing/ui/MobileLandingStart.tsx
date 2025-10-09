import Image from 'next/image';
import { StandardButton } from '@/shared/ui';

interface IProps {
  goToNext: () => void;
  handleServiceStart: () => void;
}

export default function MobileLandingStart({
  goToNext,
  handleServiceStart,
}: IProps) {
  return (
    <div
      className={
        'w-[100vw] h-[100vh] relative ' +
        'bg-[#F4F4F4]  flex flex-col justify-center items-center gap-y-[10px]'
      }
    >
      <p
        className={
          'w-[500px] text-[14px] text-[#4A4A4A] font-medium text-center mt-[20px] absolute top-[60px] left-1/2 -translate-x-1/2 select-none'
        }
      >
        팝업스토어 정보 확인부터 실시간 웨이팅까지, <br />한 번에 해결하는
        <em className={'text-main not-italic'}> ‘Spot it!’ </em>
      </p>

      <Image
        src={'/images/landing/LANDING_START_PHONE.png'}
        width={315}
        height={362}
        alt={'service-landing-image'}
        className={
          'object-fill w-[315x] absolute top-[150px] left-1/2 -translate-x-1/2'
        }
        priority
        fetchPriority="high"
      />

      <div
        className={
          'flex flex-col gap-y-[8px] absolute bottom-[30px] left-1/2 -translate-x-1/2 w-[334px]'
        }
      >
        <StandardButton
          color={'primary'}
          disabled={false}
          onClick={goToNext}
          size={'full'}
        >
          스팟잇 알아보기
        </StandardButton>
        <StandardButton
          color={'white'}
          disabled={false}
          onClick={() => handleServiceStart()}
          size={'full'}
          className={'border-main text-main'}
        >
          지금 바로 시작하기
        </StandardButton>
      </div>
    </div>
  );
}
