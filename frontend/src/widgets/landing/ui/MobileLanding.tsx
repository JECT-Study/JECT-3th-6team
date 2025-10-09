'use client';

import { useState } from 'react';
import {
  MobileGuideStepType,
  MobileStepType,
} from '@/widgets/landing/type/type';
import { useRouter } from 'next/navigation';
import MobileLandingStart from '@/widgets/landing/ui/MobileLandingStart';
import MobileStep from '@/widgets/landing/ui/MobileStep';

export default function MobileLanding() {
  const [step, setStep] = useState<MobileStepType>('START');
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
  };

  return (
    <div>
      {step === 'START' && (
        <MobileLandingStart
          goToNext={goToNext}
          handleServiceStart={handleServiceStart}
        />
      )}

      {['GUIDE_1', 'GUIDE_2', 'GUIDE_3'].includes(step) && (
        <MobileStep
          currentStep={step as MobileGuideStepType}
          onNext={goToNext}
          handleServiceStart={handleServiceStart}
        />
      )}
    </div>
  );
}
