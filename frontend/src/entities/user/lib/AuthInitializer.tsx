'use client';

import { useEffect } from 'react';
import { useUserStore } from '@/entities/user/lib/useUserStore';
import { useQuery } from '@tanstack/react-query';
import getUserApi from '@/entities/user/api/getUserApi';

export default function AuthInitializer() {
  const setUser = useUserStore(state => state.setUser);
  const clearUser = useUserStore(state => state.clearUser);

  const { data: user, isError } = useQuery({
    queryKey: ['auth', 'user'],
    queryFn: () => getUserApi(),
    retry: false,
    staleTime: 5 * 60 * 1000,
    refetchOnWindowFocus: false,
    select: data => (data ? { email: data.email, nickname: data.name } : null),
    throwOnError: false,
  });

  useEffect(() => {
    if (user) {
      setUser({
        email: user.email,
        nickname: user.nickname,
        role: 'user',
      });
    } else if (isError) {
      clearUser();
    }
  }, [user, isError, setUser, clearUser]);

  return null;
}
