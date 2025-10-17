import { useRouter } from 'next/navigation';
import { useQueryClient } from '@tanstack/react-query';

import { useUserStore } from '@/entities/user/lib/useUserStore';

import deleteUserApi from '@/features/auth/api/deleteUserApi';

export function useWithdrawalConfirm() {
  const router = useRouter();
  const queryClient = useQueryClient();

  const clearUser = useUserStore(state => state.clearUser);

  return async () => {
    try {
      await deleteUserApi();
    } catch (error) {
      console.error('탈퇴 처리 중 오류 발생:', error);
    } finally {
      clearUser();
      await queryClient.clear();
      router.push('/');
    }
  };
}
