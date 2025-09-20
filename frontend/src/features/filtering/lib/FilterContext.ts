'use client';

import { createContext, useContext } from 'react';
import useFilter from '@/features/filtering/hook/useFilter';

export type FilterContextType = ReturnType<typeof useFilter> | null;

export const FilterContext = createContext<FilterContextType>(null);

export const useFilterContext = () => {
  const context = useContext(FilterContext);
  if (!context)
    throw new Error(
      'useFilterContext는 FilterProvider 내부에 위치해야 합니다.'
    );
  return context;
};
