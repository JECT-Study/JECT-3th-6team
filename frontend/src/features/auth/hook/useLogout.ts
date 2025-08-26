'use client';

import { useRouter } from 'next/navigation';
import { useUserStore } from '@/entities/user/lib/useUserStore';
import postLogoutApi from '@/features/auth/api/postLogoutApi';
import { useQueryClient } from '@tanstack/react-query';

export function useLogout() {
  const clearUser = useUserStore(state => state.clearUser);
  const router = useRouter();

  const queryClient = useQueryClient();
  return async () => {
    try {
      await postLogoutApi();
    } catch (error) {
      console.error('로그아웃 실패:', error);
    } finally {
      // 서버 응답 성공/실패와 관계없이 로컬 상태 초기화
      clearUser();
      await queryClient.clear();
      router.push('/');
    }
  };
}
