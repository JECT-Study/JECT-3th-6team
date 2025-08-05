import { useMutation } from '@tanstack/react-query';
import postOnsiteReservationApi, {
  OnsiteReservationRequest,
} from '@/features/reservation/api/postOnsiteReservationApi';
import { RESERVATION_STORAGE_KEY } from '@/features/reservation/constants/reservationStorageKey';
import { OnsiteReservationResponse } from '@/features/reservation/type/OnsiteReservationResponse';
import { storage } from '@/shared/lib/localStorage';
import { useRouter } from 'next/navigation';
import { toast } from 'sonner';

export default function usePostReservation({ popupId }: { popupId: number }) {
  const router = useRouter();
  const query = useMutation({
    retry: false,
    mutationFn: (request: OnsiteReservationRequest) =>
      postOnsiteReservationApi(request),
    onError: error => {
      console.error('[onError]:', error);
      toast.error('현장 대기 예약에 실패했습니다.');
    },
    onSuccess: data => {
      // 로컬 스토리지에 저장
      const key = RESERVATION_STORAGE_KEY.onsite(popupId);
      storage.setJSON<OnsiteReservationResponse>(key, data);

      toast.success('대기 예약 완료!');
      router.push(`/reservation/complete/${popupId}`);
    },
  });

  return query;
}
