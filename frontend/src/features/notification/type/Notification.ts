export type RelatedResourceType =
  | { type: 'POPUP'; data: { id: number; storeName: string; address?: string } }
  | {
      type: 'WAITING';
      data: { id: number; waitingNumber: number; registeredAt?: string };
    };

export type NotificationCodeType =
  | 'WAITING_CONFIRMED'
  | 'ENTER_3TEAMS_BEFORE'
  | 'ENTER_NOW'
  | 'ENTER_TIME_OVER'
  | 'NOSHOW_FIRST'
  | 'NOSHOW_SECOND';

export default interface NotificationType {
  notificationId: number;
  notificationCode: NotificationCodeType;
  message: string;
  createdAt: string;
  isRead: boolean;
  relatedResources: RelatedResourceType[];
}
