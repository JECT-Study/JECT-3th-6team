import { usePathname, useRouter } from 'next/navigation';
import { useUserStore } from '@/entities/user/lib/useUserStore';
import { useEffect } from 'react';

export default function AuthGuard({ children }: { children: React.ReactNode }) {
  const router = useRouter();
  const pathname = usePathname();
  const isLoggedIn = useUserStore(s => s.userState.isLoggedIn);

  useEffect(() => {
    if (!isLoggedIn) router.replace(`/login?redirect_path=${pathname}`);
  }, [isLoggedIn, pathname, router]);

  if (!isLoggedIn) return null;
  return <>{children}</>;
}
