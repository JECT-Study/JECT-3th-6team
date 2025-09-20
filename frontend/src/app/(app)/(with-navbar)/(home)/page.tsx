import FilterBottomSheet from '@/features/filtering/ui/FilterBottomSheet';
import FilterSelectButtonGroup from '@/features/filtering/ui/FilterSelectButtonGroup';
import FilterKeywordSelect from '@/features/filtering/ui/FilterKeywordSelect';
import FilteredPopupListWrapper from '@/features/filtering/ui/FilteredPopupListWrapper';
import NotificationModal from '@/features/notification/ui/NotificationModal';
import ReservationWaitingFloating from '@/features/waiting/ui/ReservationWaitingFloating';
import { HomeFilterProvider } from '@/features/filtering/lib/FilterProviders';

export const dynamic = 'force-dynamic';
export const revalidate = 0;
export const fetchCache = 'default-no-store';

export default function Home() {
  return (
    <HomeFilterProvider>
      <NotificationModal />
      <ReservationWaitingFloating />
      <FilterSelectButtonGroup />
      <FilterKeywordSelect />
      <FilterBottomSheet />
      <FilteredPopupListWrapper />
    </HomeFilterProvider>
  );
}
