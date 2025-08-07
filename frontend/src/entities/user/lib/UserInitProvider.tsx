'use client';

import React, { useEffect } from 'react';

import { User, useUserStore } from '@/entities/user/lib/useUserStore';

export default function UserInitProvider({
  children,
  initialUser,
}: {
  children: React.ReactNode;
  initialUser: Omit<User, 'role'> | null;
}) {
  const setUser = useUserStore(state => state.setUser);
  const clearUser = useUserStore(state => state.clearUser);
  useEffect(() => {
    if (initialUser) {
      setUser({
        email: initialUser.email,
        nickname: initialUser.nickname,
        role: 'user',
      });
    } else {
      clearUser();
    }
  }, [initialUser]);
  return <>{children}</>;
}
