import { APIBuilder, logError } from '@/shared/lib';
import { RelatedResourceType } from '@/features/notification/type/Notification';
import { ApiError } from '@/shared/type/api';

type NotificationReadParamDto = {
  notificationId: number;
};

type NotificationReadResponseDto = {
  notificationId: number;
  notificationCode: string;
  message: string;
  createdAt: string;
  isRead: boolean;
  relatedResource: RelatedResourceType;
};

export default async function patchNotificationAsReadApi(
  param: NotificationReadParamDto
) {
  const { notificationId } = param;
  try {
    const builder = await APIBuilder.patch(
      `/notifications/${notificationId}/read`,
      {}
    )
      .timeout(5000)
      .withCredentials(true)
      .auth()
      .buildAsync();
    const response = await builder.call<NotificationReadResponseDto>();

    return response.data;
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error);
    }
    throw error;
  }
}
