'use client';

import { useMutation, useQueryClient } from '@tanstack/react-query';
import deleteNotificationApi from '@/features/notification/api/deleteNotificationApi';

export default function useDeleteNotification() {
  const queryClient = useQueryClient();
  return useMutation({
    retry: false,
    mutationFn: (notificationId: number) =>
      deleteNotificationApi(notificationId),
    onError: error => {
      console.error('[onError]:', error.message);
      toast.error('알림 삭제에 실패했습니다.');
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries(['notification', 'list']);
    },
  });
}
