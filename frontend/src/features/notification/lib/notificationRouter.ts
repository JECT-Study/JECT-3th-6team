import { NotificationCodeType } from '../type/Notification';

const NotificationCodeRouterMap: {
  [K in NotificationCodeType]: (id: number) => string;
} = {
  WAITING_CONFIRMED: id => `/waiting/${id}`,
  ENTER_3TEAMS_BEFORE: id => {
    if (process.env.NEXT_PUBLIC_ENV === 'DEVELOP') console.log(id);
    return '/history';
  },
  ENTER_NOW: id => `/reservation/detail/${id}`,
  ENTER_TIME_OVER: id => `/reservation/detail/${id}`,
};

export default NotificationCodeRouterMap;
