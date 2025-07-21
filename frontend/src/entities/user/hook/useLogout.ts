'use client';

import { useRouter } from 'next/navigation';
import { useUserStore } from '@/entities/user/lib/useUserStore';

export function useLogout() {
  const clearUser = useUserStore(state => state.clearUser);
  const router = useRouter();

  return () => {
    // ✅ 여기에 logout API 호출 로직 작성
    clearUser();
    router.push('/');
  };
}
