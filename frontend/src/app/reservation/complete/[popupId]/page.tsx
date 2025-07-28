import PageHeader from '@/shared/ui/header/PageHeader';
import { ReservationSummaryView } from '@/features/reservation';

export default async function ReservationCompletePage() {
  return (
    <div>
      <PageHeader title={'웨이팅 확정'} />
      <ReservationSummaryView />
    </div>
  );
}
