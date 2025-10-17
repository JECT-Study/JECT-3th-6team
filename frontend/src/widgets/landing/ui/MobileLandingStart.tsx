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
        'w-screen h-screen  ' +
        'bg-[#F4F4F4]  flex flex-col justify-center items-center gap-y-[10px]'
      }
    >
      <Image
        src={'/images/landing/LANDING_LOGO_BIG.svg'}
        width={67}
        height={87}
        alt={'logo-big'}
        className={''}
        priority
        fetchPriority="high"
      />
      <p
        className={
          'w-[300px] text-[14px] text-[#4A4A4A] font-medium text-center mt-[20px] select-none'
        }
      >
        팝업스토어 정보를 모아보고, 실시간 웨이팅까지 <br />
        가능한 통합 플랫폼
        <em className={'text-main not-italic'}> ‘Spot it!’ </em> 을 알아볼까요?
      </p>

      <Image
        src={'/images/landing/LANDING_START_PHONE.png'}
        width={250}
        height={315}
        alt={'service-landing-image'}
        className={'object-fill w-[250px] '}
        priority
        fetchPriority="high"
      />

      <div className={'flex flex-col gap-y-[8px] w-[90vw] max-w-[334px]'}>
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
