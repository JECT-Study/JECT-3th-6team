import NotificationType from '@/features/notification/type/Notification';

const dummyNotificationList: Array<NotificationType> = [
  {
    notificationId: 101,
    notificationCode: 'REVIEW_REQUEST',
    message:
      "오늘 '무신사 X 나이키 팝업스토어' 방문은 어떠셨나요? 간단한 리뷰만 남겨주셔도 큰 힘이 됩니다 :)",
    createdAt: '2025-07-25T15:00:00',
    isRead: false,
    relatedResource: {
      type: 'POPUP',
      id: 42,
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
];
export default dummyNotificationList;
