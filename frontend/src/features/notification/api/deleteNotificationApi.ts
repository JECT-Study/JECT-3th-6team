import { APIBuilder, logError } from '@/shared/lib';
import { ApiError } from '@/shared/type/api';

type NotificationDeleteParamDto = {
  notificationId: number;
};

export default async function deleteNotificationApi(
  param: NotificationDeleteParamDto
) {
  const { notificationId } = param;

  try {
    const builder = await APIBuilder.delete(`/notifications/${notificationId}`)
      .timeout(5000)
      .withCredentials(true)
      .auth()
      .buildAsync();
    await builder.call();
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error);
    }
    throw error;
  }
}
