import PageHeader from '@/shared/ui/header/PageHeader';
import { OnsiteReservationForm } from '@/features/reservation';

export default async function ReservationOnsitePopupPage({
  params,
}: {
  params: Promise<{ popupId: number }>;
}) {
  const { popupId } = await params;
  return (
    <div>
      <PageHeader title={'현장 대기'} />
      <OnsiteReservationForm popupId={popupId} />
    </div>
  );
}
