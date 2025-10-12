'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import LandingStart from '@/widgets/landing/ui/LandingStart';
import Step from '@/widgets/landing/ui/Step';
import LandingEnd from '@/widgets/landing/ui/LandingEnd';
import { DesktopStepType } from '@/widgets/landing/type/type';

export default function DesktopLanding() {
  const [step, setStep] = useState<DesktopStepType>('START');
  const router = useRouter();

  const handleServiceStart = () => {
    const SIX_MONTHS = 60 * 60 * 24 * 182;
    document.cookie = `seenLanding=1; Max-Age=${SIX_MONTHS}; Path=/; SameSite=Lax`;
    try {
      localStorage.setItem('spotit_seen_landing', '1');
    } catch {}
    router.replace('/');
  };

  const goToNext = () => {
    if (step === 'START') return setStep('GUIDE_1');
    if (step === 'GUIDE_1') return setStep('GUIDE_2');
    if (step === 'GUIDE_2') return setStep('GUIDE_3');
    if (step === 'GUIDE_3') return setStep('GUIDE_4');
    if (step === 'GUIDE_4') return setStep('END');
  };

  return (
    <div
      className={'w-full h-screen bg-white flex items-center justify-center '}
    >
      {step === 'START' && (
        <LandingStart
          onNext={() => setStep('GUIDE_1')}
          onStartService={() => handleServiceStart()}
        />
      )}

      {['GUIDE_1', 'GUIDE_2', 'GUIDE_3', 'GUIDE_4'].includes(step) && (
        <Step
          currentStep={step as DesktopStepType}
          onNext={goToNext}
          onStepChange={setStep}
        />
      )}
      {step === 'END' && <LandingEnd onStartService={handleServiceStart} />}
    </div>
  );
}
