import PageHeader from '@/shared/ui/header/PageHeader';
import { OnsiteReservationForm } from '@/features/reservation';

export default function ReservationOnsitePopupPage() {
  return (
    <div>
      <PageHeader title={'현장 대기'} />
      <OnsiteReservationForm />
    </div>
  );
}
