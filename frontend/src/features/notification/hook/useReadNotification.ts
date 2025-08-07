'use client';

import patchNotificationAsReadApi from '@/features/notification/api/patchNotificationAsReadApi';
import { useMutation } from '@tanstack/react-query';

// TODO : Optimistic Update 적용
export default function useReadNotification() {
  const mutation = useMutation({
    retry: false,
    mutationFn: (notificationId: number) =>
      patchNotificationAsReadApi({ notificationId }),
    onError: error => {
      console.error('[onError]:', error.message);
      toast.error('알림 내역 읽음처리에 실패했습니다.');
    },
  });

  return mutation;
}
