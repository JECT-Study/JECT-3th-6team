'use client';

import Link from 'next/link';
import IconHome from '@/assets/icons/Navigation/Icon_Home.svg';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import IconMyHistory from '@/assets/icons/Navigation/Icon_My_History.svg';
import IconSetting from '@/assets/icons/Navigation/Icon_Setting.svg';
import { usePathname } from 'next/navigation';
import { cn } from '@/lib/utils';

export default function Navbar() {
  const pathname = usePathname();

  const navItems = [
    {
      label: '홈',
      path: '/',
      icon: IconHome,
    },
    { label: '지도', path: '/map', icon: IconMap },
    { label: '내방문', path: '/history', icon: IconMyHistory },
    { label: '설정', path: '/setting', icon: IconSetting },
  ];

  return (
    <div
      className={
        'bg-white w-full max-w-[430px] min-w-[320px] py-[34px] fixed bottom-0 flex justify-around pt-1 '
      }
      style={{
        // 모바일에서 안전 영역 고려
        paddingBottom: 'calc(34px + env(safe-area-inset-bottom, 0px))',
      }}
    >
      {navItems.map((item, index) => {
        const Icon = item.icon;
        const isActive = pathname === item.path;
        const color = isActive ? 'var(--color-main)' : 'var(--color-black)';
        return (
          <Link
            href={item.path}
            key={index}
            className={'flex-col items-center justify-center'}
          >
            <Icon
              width={36}
              height={36}
              fill={color}
              className={'w-[36px] h-[36px]'}
            />
            <span
              className={cn(
                'block text-[10px] font-regular text-black select-none text-center',
                isActive ? 'text-main' : 'text-gray60'
              )}
            >
              {item.label}
            </span>
          </Link>
        );
      })}
    </div>
  );
}
