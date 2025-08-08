'use client';

import PageHeader from '@/shared/ui/header/PageHeader';
import { ReservationSummaryView } from '@/features/reservation';
import { RESERVATION_STORAGE_KEY } from '@/features/reservation/constants/reservationStorageKey';
import { storage } from '@/shared/lib/localStorage';
import { OnsiteReservationResponse } from '@/features/reservation/type/OnsiteReservationResponse';
import { useParams } from 'next/navigation';
import { useEffect } from 'react';

export default function ReservationCompletePage() {
  const { popupId } = useParams();

  const key = RESERVATION_STORAGE_KEY.onsite(Number(popupId));
  const summaryData = storage.getJSON<OnsiteReservationResponse>(key);
  // storage.clear(key);

  useEffect(() => {
    return () => {
      storage.clear(key);
    };
  }, [key]);

  if (summaryData === null) {
    return null;
  }
  return (
    <div>
      <PageHeader title={'웨이팅 확정'} />
      <ReservationSummaryView data={summaryData} />
    </div>
  );
}
