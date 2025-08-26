'use client';

import patchNotificationAsReadApi from '@/features/notification/api/patchNotificationAsReadApi';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';

// TODO : Optimistic Update 적용
export default function useReadNotification() {
  const queryClient = useQueryClient();
  const mutation = useMutation({
    retry: false,
    mutationFn: (notificationId: number) =>
      patchNotificationAsReadApi({ notificationId }),
    onError: error => {
      console.error('[onError]:', error.message);
      toast.error('알림 내역 읽음처리에 실패했습니다.');
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: ['notification', 'list'],
      });
      await queryClient.invalidateQueries({
        queryKey: ['notification', 'unread-list'],
      });
    },
  });

  return mutation;
}
