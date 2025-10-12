'use client';

import LandingTitle from '@/widgets/landing/ui/LandingTitle';
import SSRImage from '@/widgets/landing/ui/SSRImage';
import { MobileGuideStepType } from '@/widgets/landing/type/type';
import { StandardButton } from '@/shared/ui';
import { cn } from '@/lib/utils';

interface StepProps {
  currentStep: MobileGuideStepType;
  onNext: () => void;
  handleServiceStart: () => void;
}

const guideSteps: MobileGuideStepType[] = ['GUIDE_1', 'GUIDE_2', 'GUIDE_3'];

export function StepIndicator({
  currentStep,
}: {
  currentStep: MobileGuideStepType;
}) {
  const currentIndex = guideSteps.indexOf(currentStep);

  return (
    <div className="flex items-center justify-center gap-2 absolute bottom-[127px]">
      {guideSteps.map((_, idx) => (
        <div
          key={idx}
          className={cn(
            'w-2.5 h-2.5 rounded-full transition-all duration-300',
            idx === currentIndex ? 'bg-main scale-90' : 'bg-white scale-80'
          )}
        />
      ))}
    </div>
  );
}

export default function MobileStep(props: StepProps) {
  const { currentStep, onNext, handleServiceStart } = props;
  const handleNext = () => {
    if (currentStep === 'GUIDE_3') {
      handleServiceStart();
    } else {
      onNext();
    }
  };
  return (
    <div
      className={
        'w-[100vw] h-[100vh] relative bg-[#F4F4F4]  flex flex-col justify-center items-center'
      }
    >
      {/*가이드이미지*/}
      <LandingTitle
        step={currentStep as MobileGuideStepType}
        device={'MOBILE'}
      />
      <SSRImage step={currentStep as MobileGuideStepType} device={'MOBILE'} />

      {/* 인디케이터 */}
      <StepIndicator currentStep={currentStep} />

      {/*스텝 이동 버튼*/}
      <StandardButton
        onClick={handleNext}
        disabled={false}
        color={'primary'}
        size={'full'}
        className={'w-[334px] absolute bottom-10'}
      >
        {currentStep === 'GUIDE_3' ? '시작하기' : '다음'}
      </StandardButton>
    </div>
  );
}
