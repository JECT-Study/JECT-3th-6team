// Notification 객체에서  팝업스토어 명을 추출하는 함수
import NotificationType from '@/features/notification/type/Notification';

export const getStoreNameFromNotification = (
  notification: NotificationType
) => {
  const { relatedResources } = notification;

  const popupObject = relatedResources?.find(
    resource => resource.type === 'POPUP'
  );
  return popupObject?.data.storeName;
};
