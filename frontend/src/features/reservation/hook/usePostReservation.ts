import { useMutation } from '@tanstack/react-query';
import postOnsiteReservationApi, {
  OnsiteReservationRequest,
} from '@/features/reservation/api/postOnsiteReservationApi';

import { useRouter } from 'next/navigation';
import { toast } from 'sonner';

export default function usePostReservation() {
  const router = useRouter();
  return useMutation({
    retry: false,
    mutationFn: (request: OnsiteReservationRequest) =>
      postOnsiteReservationApi(request),
    onError: error => {
      console.error('[onError]:', error);
      toast.error('현장 대기 예약에 실패했습니다.');
    },
    onSuccess: data => {
      toast.success('대기 예약 완료!');
      router.push(`/reservation/complete/${data.waitingId}`);
    },
  });
}
