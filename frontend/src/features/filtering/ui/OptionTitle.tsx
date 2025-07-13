import IconCalendar from '@/assets/icons/Normal/Icon_Calendar.svg';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import IconFilter from '@/assets/icons/Normal/Icon_Filter_Gray.svg';
import React from 'react';
import { FilterType } from '@/features/filtering/hook/useFilter';

interface OptionTitleProps {
  openType: FilterType;
}

const TITLE_OPTION_MAP: Record<
  FilterType,
  { icon: React.ReactNode; title: string }
> = {
  date: {
    icon: (
      <IconCalendar
        width={22}
        height={22}
        stroke={'var(--color-gray80)'}
        fill={'var(--color-white)'}
      />
    ),
    title: '날짜',
  },
  location: {
    icon: (
      <IconMap
        width={22}
        height={22}
        fill={'var(--color-gray80)'}
        stroke={'var(--color-gray80)'}
      />
    ),
    title: '지역',
  },
  keyword: {
    icon: <IconFilter width={22} height={22} />,
    title: '필터',
  },
};

export default function OptionTitle({ openType }: OptionTitleProps) {
  return (
    <div className={'flex items-center justify-center gap-x-1 select-none'}>
      {TITLE_OPTION_MAP[openType].icon}
      <span className={'text-black font-[18px]  select-none text-center'}>
        {TITLE_OPTION_MAP[openType].title}
      </span>
    </div>
  );
}
