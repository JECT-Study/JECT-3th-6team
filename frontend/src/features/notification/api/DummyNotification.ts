import NotificationType from '@/features/notification/type/Notification';

const dummyNotificationList: Array<NotificationType> = [
  {
    notificationId: 101,
    notificationCode: 'WAITING_CONFIRMED',
    message:
      '06.28 (X) X인 웨이팅이 완료되었습니다. 현재 대기 번호를 확인해주세요!',
    createdAt: '2025-07-27T13:00:00',
    isRead: false,
    relatedResources: [
      {
        type: 'POPUP',
        data: {
          id: 1,
          storeName: '무신사 X 나이키 팝업스토어',
        },
      },
      {
        type: 'WAITING',
        data: {
          waitingNumber: 1,
          registeredAt: '2025-06-28T10:00:00',
          id: 100,
        },
      },
    ],
  },
  {
    notificationId: 99,
    notificationCode: 'ENTER_3TEAMS_BEFORE',
    message:
      '앞으로 3팀 남았습니다! 순서가 다가오니 매장 근처에서 대기해주세요!',
    createdAt: '2025-07-25T12:50:00',
    isRead: true,
    relatedResources: [
      {
        type: 'POPUP',
        data: {
          id: 1,
          storeName: '무신사 X 나이키 팝업스토어',
          address: '경기도 성남시 분당구',
        },
      },
      {
        type: 'WAITING',
        data: {
          waitingNumber: 1,
          id: 100,
        },
      },
    ],
  },
  {
    notificationId: 100,
    notificationCode: 'ENTER_NOW',
    message: '지금 매장으로 입장 부탁드립니다. 즐거운 시간 보내세요!',
    createdAt: '2025-07-25T13:00:00',
    isRead: true,
    relatedResources: [
      {
        type: 'POPUP',
        data: {
          id: 1,
          storeName: '무신사 X 나이키 팝업스토어',
          address: '경기도 성남시 분당구',
        },
      },
      {
        type: 'WAITING',
        data: {
          waitingNumber: 1,
          id: 100,
        },
      },
    ],
  },
  {
    notificationId: 98,
    notificationCode: 'ENTER_TIME_OVER',
    message:
      '입장 시간이 초과되었습니다. 빠른 입장 부탁드립니다! 입장이 지연될 경우 웨이팅이 취소될 수 있습니다.',
    createdAt: '2025-07-25T12:50:00',
    isRead: true,
    relatedResources: [
      {
        type: 'POPUP',
        data: {
          id: 1,
          storeName: '무신사 X 나이키 팝업스토어',
          address: '경기도 성남시 분당구',
        },
      },
      {
        type: 'WAITING',
        data: {
          waitingNumber: 1,
          id: 100,
        },
      },
    ],
  },
];
export default dummyNotificationList;
