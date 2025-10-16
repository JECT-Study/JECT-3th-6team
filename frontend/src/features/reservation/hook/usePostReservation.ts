import { useMutation } from '@tanstack/react-query';
import postOnsiteReservationApi, {
  OnsiteReservationRequest,
} from '@/features/reservation/api/postOnsiteReservationApi';

import { useRouter } from 'next/navigation';
import { toast } from 'sonner';
import { FORM_STORAGE_KEY } from '@/features/reservation/constants/formStorageKey';
import { storage } from '@/shared/lib/localStorage';

export default function usePostReservation({ popupId }: { popupId: number }) {
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
      const storageKey = FORM_STORAGE_KEY({
        formType: 'onsite-reservation',
        formKey: popupId,
      });
      storage.clear(storageKey);
      router.replace(`/reservation/complete/${data.waitingId}`);
    },
  });
}
