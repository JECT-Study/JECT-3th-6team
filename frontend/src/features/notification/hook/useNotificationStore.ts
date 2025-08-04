import { create } from 'zustand';
import NotificationType from '@/features/notification/type/Notification';
import dummyNotificationList from '@/features/notification/api/DummyNotification';

interface NotificationState {
  list: NotificationType[];
  add: (noti: NotificationType) => void;
  remove: (id: number) => void;
}

export const useNotificationStore = create<NotificationState>(set => ({
  list: [...dummyNotificationList],
  add: noti => set(({ list }) => ({ list: [...list, noti] })),
  remove: (id: number) =>
    set(state => ({
      list: state.list.filter(noti => noti.notificationId !== id),
    })),
}));
