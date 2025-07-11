'use client';

import { useState } from 'react';
import { MonthlyCalendar } from '@/shared/ui';

type DateRange = {
  start: Date | null;
  end: Date | null;
};

export default function Page() {
  const [selected, setSelected] = useState<Date | DateRange | null>(null);

  const handleSelect = (value: Date | DateRange) => {
    setSelected(value);
  };

  return (
    <>
      <h1> 캘린더 컴포넌트 </h1>
      <MonthlyCalendar
        mode={'single'}
        selected={selected}
        onSelected={handleSelect}
      />
    </>
  );
}
