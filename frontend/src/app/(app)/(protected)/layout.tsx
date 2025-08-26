'use client';

import { useUserHydrated } from '@/entities/user/lib/useUserStore';
import dynamic from 'next/dynamic';

const AuthGuard = dynamic(() => import('@/features/auth/lib/AuthGuard'), {
  ssr: false,
  loading: () => (
    <div className="flex h-[60vh] items-center justify-center">
      <div className="animate-pulse rounded-xl bg-gray-100 px-6 py-4 text-gray-600">
        유저정보 확인 중이에요…
      </div>
    </div>
  ),
});

export default function AuthRequiredLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const hasHydrated = useUserHydrated();

  if (!hasHydrated) return null;
  return <AuthGuard>{children}</AuthGuard>;
}
