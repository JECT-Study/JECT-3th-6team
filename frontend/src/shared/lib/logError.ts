import { ApiError } from '@/shared/type/api';

export default function logError(error: ApiError, context = '') {
  if (context) {
    console.error(`${context} 에서 에러가 발생하였습니다.`);
  }
  console.error(`${error.message}`);
}
