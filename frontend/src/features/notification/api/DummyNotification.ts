import NotificationType from '@/features/notification/type/Notification';

const dummyNotificationList: Array<NotificationType> = [
  {
    notificationId: 101,
    notificationCode: 'WAITING_CONFIRMED',
    message:
      'XX.XX (X) X인 웨이팅이 완료되었습니다. 현재 대기 번호를 확인해주세요!',
    createdAt: '2025-07-27T13:00:00',
    isRead: false,
    relatedResource: {
      type: 'WAITING',
      id: 12,
    },
    notificationTitle: '무신사 팝업',
  },
  {
    notificationId: 99,
    notificationCode: 'ENTER_3TEAMS_BEFORE',
    message:
      '앞으로 3팀 남았습니다! 순서가 다가오니 매장 근처에서 대기해주세요!',
    createdAt: '2025-07-25T12:50:00',
    isRead: true,
    relatedResource: {
      type: 'MY_VISIT_HISTORY',
      id: null,
    },
    notificationTitle: '엑시즈와이 팝업',
  },
  {
    notificationId: 100,
    notificationCode: 'ENTER_NOW',
    message: '지금 매장으로 입장 부탁드립니다. 즐거운 시간 보내세요!',
    createdAt: '2025-07-25T13:00:00',
    isRead: true,
    relatedResource: {
      type: 'WAITING',
      id: 123,
    },
    notificationTitle: '엑시즈와이 팝업',
  },
  {
    notificationId: 98,
    notificationCode: 'ENTER_TIME_OVER',
    message:
      '앞으로 3팀 남았습니다! 순서가 다가오니 매장 근처에서 대기해주세요!',
    createdAt: '2025-07-25T12:50:00',
    isRead: true,
    relatedResource: {
      type: 'WAITING',
      id: 123,
    },
    notificationTitle: '엑시즈와이 팝업',
  },
];
export default dummyNotificationList;
