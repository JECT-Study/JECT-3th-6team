import { APIBuilder, logError } from '@/shared/lib';
import { ApiError } from '@/shared/type/api';

export default async function deleteUserApi() {
  try {
    const builder = await APIBuilder.delete('/auth/me')
      .auth()
      .timeout(5000)
      .withCredentials(true)
      .buildAsync();
    await builder.call();
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error);
    }
    throw error;
  }
}
