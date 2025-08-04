import { FilterProvider } from '@/features/filtering/lib/FilterContext';
import FilterBottomSheet from '@/features/filtering/ui/FilterBottomSheet';
import FilterSelectButtonGroup from '@/features/filtering/ui/FilterSelectButtonGroup';
import FilterKeywordSelect from '@/features/filtering/ui/FilterKeywordSelect';
import FilteredPopupList from '@/features/filtering/ui/FilteredPopupList';
import { Suspense } from 'react';
import PopupCardListSuspenseFallback from '@/entities/popup/ui/PopupCardListSuspenseFallback';
import NotificationModal from '@/features/notification/ui/NotificationModal';

export default function Home() {
  return (
    <FilterProvider>
      <NotificationModal />
      <FilterSelectButtonGroup />
      <FilterKeywordSelect />
      <FilterBottomSheet />
      <Suspense fallback={<PopupCardListSuspenseFallback />}>
        <FilteredPopupList />
      </Suspense>
    </FilterProvider>
  );
}
