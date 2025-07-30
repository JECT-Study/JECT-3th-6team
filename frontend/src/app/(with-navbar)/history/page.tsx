import PopupHistoryList from '@/features/history/ui/PopupHistoryList';
import { Suspense } from 'react';
import PopupCardListSuspenseFallback from '@/entities/popup/ui/PopupCardListSuspenseFallback';

export default function HistoryPage() {
  return (
    <div>
      <Suspense fallback={<PopupCardListSuspenseFallback />}>
        <PopupHistoryList />
      </Suspense>
    </div>
  );
}
