import { useMutation } from '@tanstack/react-query';
import postOnsiteReservationApi, {
  OnsiteReservationRequest,
} from '@/features/reservation/api/postOnsiteReservationApi';
import { toast } from 'sonner';

import { useRouter } from 'next/navigation';

export default function usePostReservation({ popupId }: { popupId: number }) {
  const router = useRouter();
  const query = useMutation({
    mutationFn: (request: OnsiteReservationRequest) =>
      postOnsiteReservationApi(request),
    onError: error => {
      console.error('[onError]:', error);
      toast.error('현장 대기 예약에 실패했습니다.');
    },
    onSuccess: data => {
      // 로컬 스토리지에 저장
      localStorage.setItem('reservation', JSON.stringify(data));
      toast.success('대기 예약 완료!');
      router.push(`/reservation/complete/${popupId}`);
    },
  });

  return query;
}
