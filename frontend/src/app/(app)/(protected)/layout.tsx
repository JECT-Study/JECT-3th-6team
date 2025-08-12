'use client';

import { useUserHydrated } from '@/entities/user/lib/useUserStore';
import dynamic from 'next/dynamic';

const AuthGuard = dynamic(() => import('@/features/auth/lib/AuthGuard'), {
  ssr: false,
  loading: () => <div className="p-6 animate-pulse">Loading…</div>,
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
