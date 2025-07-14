'use client';

import { createContext, useContext } from 'react';
import { addDays } from 'date-fns';
import useFilter from '@/features/filtering/hook/useFilter';
import { FilterState } from '@/features/filtering/hook/type';

const FilterContext = createContext<ReturnType<typeof useFilter> | null>(null);

export const FilterProvider = ({ children }: { children: React.ReactNode }) => {
  const today = new Date();
  const aWeekLater = addDays(today, 6);
  const initialFilter: FilterState = {
    date: {
      start: today,
      end: aWeekLater,
    },
    location: '전국',
    keyword: {
      popupType: [],
      category: [],
    },
  };

  const filter = useFilter(initialFilter);
  return (
    <FilterContext.Provider value={filter}>{children}</FilterContext.Provider>
  );
};

export const useFilterContext = () => {
  const context = useContext(FilterContext);
  if (!context)
    throw new Error(
      'useFilterContext는 FilterProvider 내부에 위치해야 합니다.'
    );
  return context;
};
