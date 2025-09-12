'use client';

import { useMutation, useQueryClient } from '@tanstack/react-query';
import deleteNotificationApi from '@/features/notification/api/deleteNotificationApi';
import { toast } from 'sonner';
import NotificationType from '@/features/notification/type/Notification';
import { logger } from '@/shared/lib';

const NOTIFICATION_LIST_QUERY_KEY = [
  'notification',
  'list',
  { readStatus: 'ALL', sort: 'UNREAD_FIRST' },
] as const;

type NotificationListData = {
  pages: {
    content: NotificationType[];
    hasNext: boolean;
    lastNotificationId: number;
  }[];
  pageParams: unknown[];
};

export default function useDeleteNotification() {
  const queryClient = useQueryClient();
  return useMutation<
    unknown, // TData (delete API 응답, 지금은 신경 안 씀)
    Error, // TError
    number, // TVariables (notificationId)
    { prevData?: NotificationListData } // TContext
  >({
    retry: false,
    mutationFn: (notificationId: number) =>
      deleteNotificationApi({ notificationId }),
    // mutationFn이 호출되기 지직전에 실행됨
    onMutate: async (notificationId: number) => {
      performance.mark(`delete_${notificationId}_start`);
      // 1. 현재 쿼리 백업
      const prevData = queryClient.getQueryData<NotificationListData>(
        NOTIFICATION_LIST_QUERY_KEY
      );

      // 2. 알림 목록에서 해당 ID를 제거
      if (prevData) {
        queryClient.setQueryData(NOTIFICATION_LIST_QUERY_KEY, {
          ...prevData,
          pages: prevData.pages.map(page => ({
            ...page,
            content: page.content.filter(
              n => n.notificationId !== notificationId
            ),
          })),
        });
      }

      performance.mark(`delete_${notificationId}_end`);
      performance.measure(
        `delete_${notificationId}_perceived_latency`,
        `delete_${notificationId}_start`,
        `delete_${notificationId}_end`
      );

      const duration = performance
        .getEntriesByName(`delete_${notificationId}_perceived_latency`)
        .at(-1)?.duration;

      console.log(`[Perf] perceived latency: ${duration} ms`);

      // 3. 에러시 롤백
      return { prevData };
    },
    onError: (error, notificationId, context) => {
      logger.error('[onError]:', error.message);
      toast.error('알림 삭제에 실패했습니다.');

      if (context?.prevData) {
        queryClient.setQueryData(NOTIFICATION_LIST_QUERY_KEY, context.prevData);
      }
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: NOTIFICATION_LIST_QUERY_KEY,
      });
    },
    // onSettled: refetch 완료 후 호출됨
    onSettled: (data, error, notificationId) => {
      performance.mark(`delete_${notificationId}_consistency_end`);
      performance.measure(
        `delete_${notificationId}_consistency_latency`,
        `delete_${notificationId}_start`,
        `delete_${notificationId}_consistency_end`
      );

      const duration = performance
        .getEntriesByName(`delete_${notificationId}_consistency_latency`)
        .at(-1)?.duration;

      console.log(`[Perf] consistency latency: ${duration} ms`);
    },
  });
}
