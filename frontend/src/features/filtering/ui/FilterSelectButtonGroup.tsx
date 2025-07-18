'use client';

import { SelectButton } from '@/shared/ui';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import IconCalendar from '@/assets/icons/Normal/Icon_Calendar.svg';
import { dateToPeriodString } from '@/entities/popup/lib/dateToPeriodString';
import { useFilterContext } from '../lib/FilterContext';

export default function FilterSelectButtonGroup() {
  const { filter, handleOpen } = useFilterContext();

  return (
    <div className="flex items-center gap-x-3 px-4.5">
      {/* 날짜 필터 */}
      <SelectButton
        Icon={
          <IconCalendar
            width={22}
            height={22}
            fill="var(--color-white)"
            stroke="var(--color-main)"
          />
        }
        label={dateToPeriodString(filter.date.start, filter.date.end!)}
        onClick={() => handleOpen('date')}
      />

      {/* 지역 필터 */}
      <SelectButton
        Icon={<IconMap width={22} height={22} fill="var(--color-main)" />}
        label={filter.location}
        onClick={() => handleOpen('location')}
      />
    </div>
  );
}
