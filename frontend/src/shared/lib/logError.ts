import { ApiError } from '@/shared/type/api';
import { logger } from '@/shared/lib/index';

export default function logError(error: ApiError, context = '') {
  if (context) {
    logger.error(`${context} 에서 에러가 발생하였습니다.`);
  }
  logger.error(`${error.message}`);
}
