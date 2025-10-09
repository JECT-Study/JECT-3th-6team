import Image from 'next/image';
import {
  DesktopGuideStepType,
  MobileGuideStepType,
} from '@/widgets/landing/type/type';
import { cn } from '@/lib/utils';

type Props =
  | {
      device: 'MOBILE';
      step: MobileGuideStepType;
    }
  | {
      device: 'DESKTOP';
      step: DesktopGuideStepType;
    };

export default function SSRImage({ step, device }: Props) {
  if (device === 'DESKTOP') {
    return (
      <Image
        src={`/images/landing/LANDING_${step}.png`}
        alt="guide image"
        width={1000}
        height={750}
        priority
        fetchPriority="high"
        sizes="(max-width: 1024px) 90vw, 1000px"
      />
    );
  }

  if (device === 'MOBILE') {
    return (
      <Image
        src={`/images/landing/LANDING_MOBILE_${step}.png`}
        alt="guide image"
        width={280}
        height={400}
        priority
        fetchPriority="high"
        className={cn(
          step === 'GUIDE_1' && 'w-screen h-auto',
          step === 'GUIDE_2' && 'w-[250px] h-auto ',
          step === 'GUIDE_3' && 'w-[280px] h-auto'
        )}
      />
    );
  }
}
