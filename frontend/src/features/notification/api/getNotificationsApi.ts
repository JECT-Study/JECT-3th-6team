import { APIBuilder, logError } from '@/shared/lib';
import NotificationType from '@/features/notification/type/Notification';
import { ApiError } from '@/shared/type/api';

export type NotificationRequest = {
  size?: number;
  readStatus?: 'READ' | 'UNREAD' | 'ALL';
  sort?: 'UNREAD_FIRST' | 'TIME_DESC';
  lastNotificationId?: number;
};

export type NotificationResponse = {
  content: NotificationType[];
  lastNotificationId: number;
  hasNext: boolean;
};

export default async function getNotificationsApi(
  request: NotificationRequest
): Promise<NotificationResponse> {
  try {
    const builder = await APIBuilder.get('/notifications')
      .params(request)
      .timeout(5000)
      .withCredentials(true)
      .auth()
      .buildAsync();
    const response = await builder.call<NotificationResponse>();
    return response.data;
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error, '알림 목록 조회 과정');
    }
    throw error;
  }
}
