import { StepType } from '@/app/landing/page';
import { StandardButton } from '@/shared/ui';
import Image from 'next/image';
import SSRImage from '@/widgets/landing/ui/SSRImage';
import LandingTitle, { GuideStepType } from '@/widgets/landing/ui/LandingTitle';

interface StepProps {
  currentStep: StepType;
  onNext: () => void;
  onStepChange: (step: StepType) => void;
}

const guideSteps: StepType[] = ['GUIDE_1', 'GUIDE_2', 'GUIDE_3', 'GUIDE_4'];

export default function Step(props: StepProps) {
  const { currentStep, onNext, onStepChange } = props;
  return (
    <div className={'grid grid-cols-[1fr_170px] gap-x-9 '}>
      <div className={'relative '}>
        {/*가이드이미지*/}
        <LandingTitle step={currentStep as GuideStepType} />
        <SSRImage step={currentStep} />

        {/* 인디케이터 */}
        <div className="absolute -bottom-5 left-1/2 -translate-x-1/2  flex items-center gap-x-3 bg-white rounded-full w-[452px] h-[48px] justify-around">
          {guideSteps.map((step, index) => {
            const isActive = step === currentStep;
            return (
              <div key={step} className="relative flex items-center">
                {/* 버튼 */}
                <button
                  onClick={() => onStepChange(step)}
                  className={`relative z-10 w-[30px] h-[30px] rounded-full border flex justify-center items-center transition-colors ${
                    isActive
                      ? 'bg-[#787878] text-white'
                      : 'bg-[#F5F5F5] border-[#DCDCDC] hover:bg-gray-200 text-black'
                  }`}
                >
                  {index + 1}
                </button>

                {/* 버튼 뒤로 이어지는 선 */}
                {index < guideSteps.length - 1 && (
                  <div className="absolute top-1/2 left-[45px] w-[50px] h-[4px] bg-[#F4F4F4] rounded full -translate-y-1/2"></div>
                )}
              </div>
            );
          })}
        </div>
      </div>

      {/*사이드 바*/}
      <div className={'h-full flex flex-col justify-between '}>
        <Image
          src={'/images/landing/LANDING_LOGO_SMALL.svg'}
          width={106}
          height={36}
          alt={'small-logo'}
        />
        <div className={'flex-1'}></div>
        <StandardButton
          onClick={onNext}
          disabled={false}
          color={'primary'}
          className={'mb-[20px]'}
        >
          다음
        </StandardButton>
      </div>
    </div>
  );
}
