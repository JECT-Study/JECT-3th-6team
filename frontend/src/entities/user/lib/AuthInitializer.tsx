import getUserApi from '../api/getUserApi';
import UserInitProvider from '@/entities/user/lib/UserInitProvider';

export default async function AuthInitializer({
  children,
}: {
  children: React.ReactNode;
}) {
  let user = null;
  try {
    user = await getUserApi();
  } catch (error) {
    console.error('유저정보 조회에 실패하였습니다.');
    console.error(error);
  }

  return <UserInitProvider initialUser={user}>{children}</UserInitProvider>;
}
