import { NotificationCodeType } from '../type/Notification';

const NotificationCodeRouterMap: {
  [K in NotificationCodeType]: (id: number) => string;
} = {
  WAITING_CONFIRMED: id => `/waiting/${id}`,
  ENTER_3TEAMS_BEFORE: id => '/history',
  ENTER_NOW: id => `/reservation/detail/${id}`,
  ENTER_TIME_OVER: id => `/reservation/detail/${id}`,
};

export default NotificationCodeRouterMap;
