import Image from 'next/image';
import { StepType } from '@/app/landing/page';

export default function SSRImage({ step }: { step: StepType }) {
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
