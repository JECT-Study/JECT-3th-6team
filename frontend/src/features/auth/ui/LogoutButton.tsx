'use client';

import { StandardButton } from '@/shared/ui';
import { useLogout } from '@/features/auth/hook/useLogout';

export const LogoutButton = () => {
  const logout = useLogout();

  return (
    <StandardButton onClick={logout} color="secondary" disabled={false}>
      로그아웃
    </StandardButton>
  );
};
