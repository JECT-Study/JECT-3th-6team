'use client';

import ReservationDetailView from './ReservationDetailView';
import useWaitingDetail from '@/features/waiting/hook/useWaitingDetail';
import { usePathname } from 'next/navigation';

export default function ReservationDetail() {
  const pathname = usePathname(); // "/reservation/detail/100"
  const segments = pathname.split('/'); // ["", "reservation", "detail", "100"]
  const waitingId = segments[3]; // "100"

  const query = useWaitingDetail(Number(waitingId));

  return <ReservationDetailView data={query.data} />;
}
