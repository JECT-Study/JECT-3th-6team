import { RelatedResourceType } from '@/features/notification/type/Notification';

// NotificationType 내의 RelatedResource에 포함된 Id를 추출하는 함수
export const getIdFromNotification = ({
  relatedResource,
  type,
}: {
  relatedResource: RelatedResourceType[];
  type: 'POPUP' | 'WAITING';
}) => {
  const dataObject = relatedResource.find(resource => resource.type === type);
  return dataObject?.data.id;
};
