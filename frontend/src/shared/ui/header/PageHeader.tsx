'use client';

import { useRouter } from 'next/navigation';
import Image from 'next/image';
import BackIcon from '/public/icons/Normal/Icon_Bracket_Left.svg';

export default function PageHeader({ title }: { title: string }) {
  const router = useRouter();

  return (
    <div
      className={
        "flex items-center justify-between px-5 after:content-[''] after:block after:h-[75px] after:invisible"
      }
    >
      <button onClick={() => router.back()}>
        <Image
          src={BackIcon}
          width={24}
          height={24}
          objectFit={'cover'}
          alt={'back icon'}
        />
      </button>
      <span className={'font-medium text-base text-black select-none'}>
        {title}
      </span>
    </div>
  );
}
