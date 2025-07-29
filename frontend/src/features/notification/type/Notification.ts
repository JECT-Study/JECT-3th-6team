export type RelatedResourceType = {
  type: 'POPUP' | 'WAITING' | 'MY_VISIT_HISTORY';
  id: number | null;
};

export type NotificationCodeType =
  | 'WAITING_CONFIRMED'
  | 'ENTER_3TEAMS_BEFORE'
  | 'ENTER_NOW'
  | 'ENTER_TIME_OVER';

export default interface NotificationType {
  notificationId: number;
  notificationCode: NotificationCodeType;
  message: string;
  createdAt: string;
  isRead: boolean;
  relatedResource: RelatedResourceType;
  // TODO : 백엔드와 논의 후 필드명 수정
  notificationTitle: string;
}
