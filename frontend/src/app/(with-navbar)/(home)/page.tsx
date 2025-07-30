import { FilterProvider } from '@/features/filtering/lib/FilterContext';
import FilterBottomSheet from '@/features/filtering/ui/FilterBottomSheet';
import FilterSelectButtonGroup from '@/features/filtering/ui/FilterSelectButtonGroup';
import FilterKeywordSelect from '@/features/filtering/ui/FilterKeywordSelect';
import FilteredPopupListWrapper from '@/features/filtering/ui/FilteredPopupListWrapper';

export default function Home() {
  return (
    <FilterProvider>
      <FilterSelectButtonGroup />
      <FilterKeywordSelect />
      <FilterBottomSheet />
      <FilteredPopupListWrapper />
    </FilterProvider>
  );
}
