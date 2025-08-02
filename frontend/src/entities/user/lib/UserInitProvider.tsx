'use client';

import React, { useEffect } from 'react';
import { UserResponse } from '@/entities/user/api/getUserApi';
import { useUserStore } from '@/entities/user/lib/useUserStore';

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
        nickname: initialUser.nickname,
        role: 'user',
      });
      console.log('유저정보 업데이트');
    }
  }, [initialUser]);
  return <>{children}</>;
}
