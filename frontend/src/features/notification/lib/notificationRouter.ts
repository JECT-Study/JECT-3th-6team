import { NotificationCodeType } from '../type/Notification';
import logger from '@/shared/lib/logger';

const NotificationCodeRouterMap: {
  [K in NotificationCodeType]: (id: number) => string;
} = {
  WAITING_CONFIRMED: id => `/waiting/${id}`,
  ENTER_3TEAMS_BEFORE: id => {
    logger.debug(id);
    return '/history';
  },
  ENTER_NOW: id => `/reservation/detail/${id}`, // 웨이팅 상세
  ENTER_TIME_OVER: id => `/reservation/detail/${id}`, // 웨이팅 상세
  NOSHOW_FIRST: id => `/reservation/detail/${id}`, // 웨이팅 상세
  NOSHOW_SECOND: id => {
    logger.debug(id);
    return '/';
  }, // 홈
};

export default NotificationCodeRouterMap;
