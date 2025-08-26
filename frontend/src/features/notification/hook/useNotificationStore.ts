import { create } from 'zustand';
import NotificationType from '@/features/notification/type/Notification';

interface NotificationState {
  list: NotificationType[];
  add: (noti: NotificationType) => void;
  remove: (id: number) => void;
}

export const useNotificationStore = create<NotificationState>(set => ({
  list: [],
  add: noti => set(({ list }) => ({ list: [...list, noti] })),
  remove: (id: number) =>
    set(state => ({
      list: state.list.filter(noti => noti.notificationId !== id),
    })),
}));
