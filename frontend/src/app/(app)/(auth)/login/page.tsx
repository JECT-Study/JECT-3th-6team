'use client';

import PageHeader from '@/shared/ui/header/PageHeader';
import { Login } from '@/features/auth';
import dynamic from 'next/dynamic';
import PublicGuard from '@/features/auth/lib/PublicGuard';

const PublicGuardClient = dynamic(() => Promise.resolve(PublicGuard), {
  ssr: false,
  loading: () => (
    <div className="flex h-[60vh] items-center justify-center">
      <div className="animate-pulse rounded-xl bg-gray-100 px-6 py-4 text-gray-600">
        로그인 여부 확인 중…
      </div>
    </div>
  ),
});

export default function LoginPage() {
  return (
    <PublicGuardClient>
      <PageHeader title={'로그인'} />
      <Login />
    </PublicGuardClient>
  );
}
