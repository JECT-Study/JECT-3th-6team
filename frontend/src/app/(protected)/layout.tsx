'use client';

import { useUserStore } from '@/entities/user/lib/useUserStore';
import { usePathname, useRouter } from 'next/navigation';
import { useEffect } from 'react';

export default function AuthRequiredLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const isLoggedIn = useUserStore(state => state.userState.isLoggedIn);
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    if (!isLoggedIn) {
      router.replace(`/login?redirect_path=${pathname}`);
    }
  }, [isLoggedIn, pathname, router]);

  if (!isLoggedIn) return null; // 라우팅 전까지 아무것도 렌더링하지 않음

  return <>{children}</>;
}
