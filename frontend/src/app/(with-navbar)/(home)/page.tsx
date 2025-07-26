import { FilterProvider } from '@/features/filtering/lib/FilterContext';
import FilterBottomSheet from '@/features/filtering/ui/FilterBottomSheet';
import FilterSelectButtonGroup from '@/features/filtering/ui/FilterSelectButtonGroup';
import FilterKeywordSelect from '@/features/filtering/ui/FilterKeywordSelect';
import FilteredPopupList from '@/features/filtering/ui/FilteredPopupList';

export default function Home() {
  return (
    <FilterProvider>
      <FilterSelectButtonGroup />
      <FilterKeywordSelect />
      <FilterBottomSheet />
      <FilteredPopupList />
    </FilterProvider>
  );
}
