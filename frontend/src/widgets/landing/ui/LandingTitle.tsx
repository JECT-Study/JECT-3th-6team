import {
  DesktopGuideStepType,
  MobileGuideStepType,
} from '@/widgets/landing/type/type';

type Props =
  | {
      device: 'MOBILE';
      step: MobileGuideStepType;
    }
  | {
      device: 'DESKTOP';
      step: DesktopGuideStepType;
    };

const DESKTOP_MESSAGE: Record<
  DesktopGuideStepType,
  { gray: string; main: string }
> = {
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

const MOBILE_MESSAGE: Record<
  MobileGuideStepType,
  { first: string; second: string }
> = {
  GUIDE_1: {
    first: '원하는 날짜와 지역, 필터를 선택하고,',
    second: '원하는 팝업스토어를 찾을 수 있어요.',
  },
  GUIDE_2: {
    first: '원하는 팝업 스토어에 미리',
    second: '웨이팅을 등록할 수 있어요.',
  },
  GUIDE_3: {
    first: '실시간 웨이팅을 하고',
    second: '입장 알림에 따라 입장해주세요.',
  },
} as const;

const Badge: Record<DesktopGuideStepType, string> = {
  GUIDE_1: 'Guide 1',
  GUIDE_2: 'Guide 2',
  GUIDE_3: 'Guide 3',
  GUIDE_4: 'Guide 4',
} as const;

export default function LandingTitle({ step, device }: Props) {
  return (
    <div
      className={
        'w-fit flex gap-x-[20px] items-center justify-center select-none '
      }
    >
      {device === 'DESKTOP' && (
        <div
          className={
            'text-[20px] font-semibold text-main bg-white  h-[46px] w-[119px] flex justify-center items-center rounded-[8px] select-none'
          }
        >
          {Badge[step]}
        </div>
      )}

      <div className={'w-fit whitespace-nowrap select-none'}>
        {device === 'DESKTOP' && (
          <div
            className={'font-medium text-[24px] flex items-center gap-x-[5px]'}
          >
            <span className={'text-[#4A4A4A]'}>
              {DESKTOP_MESSAGE[step].gray}{' '}
            </span>
            <span className={'text-main'}>{DESKTOP_MESSAGE[step].main}</span>
          </div>
        )}

        {device === 'MOBILE' && (
          <div
            className={
              'font-medium text-[16px] flex flex-col gap-y-[10px] items-center justify-center'
            }
          >
            <span className={'text-gray80'}>{MOBILE_MESSAGE[step].first}</span>
            <span className={'text-gray80'}>{MOBILE_MESSAGE[step].second}</span>
          </div>
        )}
      </div>
    </div>
  );
}
