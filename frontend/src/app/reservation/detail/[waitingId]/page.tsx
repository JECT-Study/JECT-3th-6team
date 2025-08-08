import PageHeader from '@/shared/ui/header/PageHeader';
import ReservationDetailWrapper from '@/features/reservation/ui/ReservationDetailWrapper';

export default async function ReservationDetailPage() {
  return (
    <div>
      <PageHeader title={'예약 내역 확인'} />
      <ReservationDetailWrapper />
    </div>
  );
}
