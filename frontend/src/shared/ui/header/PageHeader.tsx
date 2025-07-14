'use client';

import { useRouter } from 'next/navigation';
import IconBack from '@/assets/icons/Normal/Icon_Bracket_Left.svg';

export default function PageHeader({ title }: { title: string }) {
  const router = useRouter();

  return (
    <div
      className={
        "flex items-center justify-between px-5 after:content-[''] after:block after:h-[75px] after:invisible"
      }
    >
      <button onClick={() => router.back()}>
        <IconBack width={24} height={24} fill={'var(--color-gray60)'} />
      </button>
      <span className={'font-medium text-base text-black select-none'}>
        {title}
      </span>
    </div>
  );
}
