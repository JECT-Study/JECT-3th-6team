import PageHeader from '@/shared/ui/header/PageHeader';
import OnsiteReservationContainer from '@/features/reservation/ui/OnsiteReservationContainer';

export default async function ReservationOnsitePopupPage() {
  return (
    <div>
      <PageHeader title={'현장 대기'} />
      <OnsiteReservationContainer />
    </div>
  );
}
