import { APIBuilder, logError } from '@/shared/lib';
import { ApiError } from '@/shared/type/api';
import { UserResponse } from '@/entities/user/type/UserResponse';

export default async function getUserApi() {
  try {
    const response = await (
      await APIBuilder.get('/auth/me')
        .timeout(5000)
        .withCredentials(true)
        .auth()
        .buildAsync()
    ).call<UserResponse>();

    return response.data;
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error, '사용자 정보 조회 과정');
    }
    throw error;
  }
}
