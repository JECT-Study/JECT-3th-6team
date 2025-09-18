'use client';

import { addDays } from 'date-fns';
import { FilterContext } from '@/features/filtering/lib/FilterContext';
import { FilterState } from '@/features/filtering/hook/type';
import useFilter from '@/features/filtering/hook/useFilter';
import useURLFilterSync from '@/features/filtering/hook/useURLFilterSync';

const initialFilter: FilterState = {
  date: {
    start: new Date(),
    end: addDays(new Date(), 6),
  },
  location: '전국',
  keyword: {
    popupType: [],
    category: [],
  },
};

const HomeFilterProvider = ({ children }: { children: React.ReactNode }) => {
  const filter = useFilter(initialFilter);
  // url에 필터 상태를 동기화
  useURLFilterSync({
    filter: filter.filter,
    setFilter: filter.setFilter,
    initialFilter,
  });

  return (
    <FilterContext.Provider value={filter}>{children}</FilterContext.Provider>
  );
};

const MapFilterProvider = ({ children }: { children: React.ReactNode }) => {
  const filter = useFilter(initialFilter);

  return (
    <FilterContext.Provider value={filter}>{children}</FilterContext.Provider>
  );
};
export { HomeFilterProvider, MapFilterProvider };
