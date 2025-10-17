'use client';

import {
  useUserHydrated,
  useUserStore,
} from '@/entities/user/lib/useUserStore';
import { useRouter } from 'next/navigation';
import React, { useEffect } from 'react';

export default function PublicGuard({
  children,
}: {
  children: React.ReactNode;
}) {
  const router = useRouter();
  const hasHydrated = useUserHydrated();
  const isLoggedIn = useUserStore(s => s.userState.isLoggedIn);

  useEffect(() => {
    if (!hasHydrated) return;
    if (isLoggedIn) {
      if (window.history.length > 1) {
        // 이전 히스토리가 있으면 뒤로가기
        router.back();
      } else {
        // 없으면 fallback
        router.replace('/');
      }
    }
  }, [hasHydrated, isLoggedIn]);

  if (!hasHydrated) return null;
  if (isLoggedIn) {
    return null;
  }

  return <>{children}</>;
}
