'use client';

import React, { useEffect } from 'react';

import { useUserStore } from '@/entities/user/lib/useUserStore';
import { UserResponse } from '@/entities/user/type/UserResponse';

export default function UserInitProvider({
  children,
  initialUser,
}: {
  children: React.ReactNode;
  initialUser: UserResponse | null;
}) {
  const setUser = useUserStore(state => state.setUser);

  useEffect(() => {
    if (initialUser) {
      setUser({
        email: initialUser.email,
        nickname: initialUser.name,
        role: 'user',
      });
    }
  }, [initialUser]);
  return <>{children}</>;
}
