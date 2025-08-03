import { APIBuilder, logError } from '@/shared/lib';
import { ApiError } from '@/shared/type/api';

export default async function postLogoutApi() {
  try {
    const builder = await APIBuilder.post('/auth/logout', {})
      .auth()
      .timeout(5000)
      .withCredentials(true)
      .buildAsync();
    await builder.call<{ message: string }>();
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error);
    }
    throw error;
  }
}
