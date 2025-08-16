import { StepType } from '@/app/landing/page';

export type GuideStepType = Exclude<StepType, 'START' | 'END'>;
interface Props {
  step: GuideStepType;
}

const MESSAGE: Record<GuideStepType, { gray: string; main: string }> = {
  GUIDE_1: {
    gray: '원하는 날짜와 지역, 필터를 선택하고,',
    main: '딱 맞는 팝업스토어를 찾아보세요!',
  },
  GUIDE_2: {
    gray: '지도 탭에서',
    main: '내 주변에 어떤 팝업스토어들이 있는지 둘러보세요!',
  },
  GUIDE_3: {
    gray: '원하는 팝업에 방문하고',
    main: '스팟잇에서 웨이팅을 등록하세요!',
  },
  GUIDE_4: {
    gray: '실시간 웨이팅 알림을 확인하고',
    main: '순서에 맞춰 입장하세요!',
  },
} as const;

const Badge: Record<GuideStepType, string> = {
  GUIDE_1: 'Guide 1',
  GUIDE_2: 'Guide 2',
  GUIDE_3: 'Guide 3',
  GUIDE_4: 'Guide 4',
} as const;

export default function LandingTitle({ step }: Props) {
  return (
    <div
      className={
        'w-fit flex gap-x-[20px] absolute top-[65px] left-1/2 transform -translate-x-1/2'
      }
    >
      <div
        className={
          'text-[20px] font-semibold text-main bg-white  h-[46px] w-[119px] flex justify-center items-center rounded-[8px] select-none'
        }
      >
        {Badge[step]}
      </div>
      <div
        className={
          'font-medium text-[24px] w-fit whitespace-nowrap flex items-center gap-x-[5px] select-none'
        }
      >
        <span className={'text-[#4A4A4A]'}>{MESSAGE[step].gray} </span>
        <span className={'text-main'}>{MESSAGE[step].main}</span>
      </div>
    </div>
  );
}
