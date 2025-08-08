import PageHeader from '@/shared/ui/header/PageHeader';
import { ReservationSummaryView } from '@/features/reservation';
import { RESERVATION_STORAGE_KEY } from '@/features/reservation/constants/reservationStorageKey';
import { storage } from '@/shared/lib/localStorage';
import { OnsiteReservationResponse } from '@/features/reservation/type/OnsiteReservationResponse';

export default async function ReservationCompletePage({
  params,
}: {
  params: Promise<{ popupId: number }>;
}) {
  const { popupId } = await params;

  // ? : key, popupId로 바로 데이터조회가 되도록 추상화계층이 더 필요할까요
  const key = RESERVATION_STORAGE_KEY.onsite(popupId);
  const summaryData = storage.getJSON<OnsiteReservationResponse>(key);
  storage.clear(key);

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
