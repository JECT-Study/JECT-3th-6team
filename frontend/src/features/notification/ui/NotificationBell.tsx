'use client';
import BellIcon from '@/assets/icons/Normal/Icon_Bell.svg';
import Link from 'next/link';
import useNotificationListener from '@/features/notification/hook/useNotificationListener';

export default function NotificationBell() {
  // TODO 알림여부에 따라 링 보여지는 여부가 달라짐
  useNotificationListener();
  return (
    <div className={'relative'}>
      <Link href={'/notify'} className={'absolute top-0 right-0 z-3'}>
        <BellIcon fill={'var(--color-main)'} width={28} height={28} />
      </Link>
      <span className="relative flex size-2 z-10">
        <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-white opacity-75"></span>
        <span className="relative inline-flex size-2 rounded-full bg-red"></span>
      </span>
    </div>
  );
}
