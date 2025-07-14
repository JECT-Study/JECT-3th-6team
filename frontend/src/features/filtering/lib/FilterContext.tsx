'use client';

import { createContext, useContext } from 'react';
import useFilter from '@/features/filtering/hook/useFilter';

const FilterContext = createContext<ReturnType<typeof useFilter> | null>(null);

export const FilterProvider = ({ children }: { children: React.ReactNode }) => {
  const filter = useFilter();
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
