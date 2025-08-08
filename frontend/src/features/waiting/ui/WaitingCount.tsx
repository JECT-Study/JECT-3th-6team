'use client';

import useWaitingDetail from '@/features/waiting/hook/useWaitingDetail';
import { usePathname } from 'next/navigation';
import WaitingCountView from '@/features/waiting/ui/WaitingCountView';

export default function WaitingCount() {
  const pathname = usePathname(); // "/waiting/100"
  const segments = pathname.split('/'); // ["", "waiting", "100"]
  const waitingId = segments[2]; // "100"

  const query = useWaitingDetail(Number(waitingId));

  return <WaitingCountView data={query.data} />;
}
