'use client';

import { FilterProvider } from '@/features/filtering/lib/FilterContext';
import FilterBottomSheet from '@/features/filtering/ui/FilterBottomSheet';
import FilterGroupMapContainer from '@/features/map/ui/FilterGroupMapContainer';

export default function MapPage() {
  return (
    <FilterProvider>
      <FilterGroupMapContainer />
      <FilterBottomSheet />
    </FilterProvider>
  );
}
