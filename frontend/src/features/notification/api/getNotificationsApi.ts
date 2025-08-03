import { APIBuilder, logError } from '@/shared/lib';
import NotificationType from '@/features/notification/type/Notification';
import { ApiError } from '@/shared/type/api';

type NotificationRequest = {
  size: number;
  lastNotificationId: number;
  readStatus: 'READ' | 'UNREAD' | 'ALL';
  sort: 'UNREAD_FIRST' | 'TIME_DESC';
};

type NotificationResponse = {
  contents: NotificationType[];
  lastNotificationId: number;
  hasNext: boolean;
};

export default async function getNotificationsApi(
  request: NotificationRequest
) {
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
