'use client';

import { useUserStore } from '@/entities/user/lib/useUserStore';

export function handleUnauthorized(): void {
  // 1) 전역 상태 초기화
  const { clearUser } = useUserStore.getState();
  clearUser();

  // 2) 로그인으로 이동 (현재 경로 redirect_path param으로 보존)
  if (typeof window !== 'undefined') {
    const isLogin = window.location.pathname.startsWith('/login'); // 현재 페이지가 login 페이지 인지 확인
    const next = encodeURIComponent(
      window.location.pathname + window.location.search
    );
    if (!isLogin) {
      window.location.assign(`/login?redirect_path=${next}`);
    }
  }
}
