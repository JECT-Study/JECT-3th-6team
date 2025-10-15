'use client';

import { useState } from 'react';
import { StandardButton } from '@/shared/ui';
import { LogoutConfirmModal } from '@/features/auth/ui/LogoutConfirmModal';

export const LogoutButton = () => {
  const [isOpen, setIsOpen] = useState<boolean>(false);

  return (
    <>
      <StandardButton
        onClick={() => {
          setIsOpen(true);
        }}
        color="secondary"
        disabled={false}
      >
        로그아웃
      </StandardButton>

      <LogoutConfirmModal open={isOpen} onClose={() => setIsOpen(false)} />
    </>
  );
};
