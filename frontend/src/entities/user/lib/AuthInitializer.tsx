'use client';
import { useQuery } from '@tanstack/react-query';
import getUserApi from '../api/getUserApi';
import UserInitProvider from '@/entities/user/lib/UserInitProvider';

export default function AuthInitializer({
  children,
}: {
  children: React.ReactNode;
}) {
  const {
    data: user,
    isLoading,
    isError,
  } = useQuery({
    queryKey: ['auth', 'user'],
    queryFn: getUserApi,
    retry: false,
    staleTime: 5 * 60 * 1000, // 5분
    gcTime: 30 * 60 * 1000, // 30분
    refetchOnWindowFocus: false,
    throwOnError: false,
    select: data => (data ? { email: data.email, nickname: data.name } : null),
  });

  if (isLoading) {
    return null;
  }
  if (isError) {
    return <UserInitProvider initialUser={null}>{children}</UserInitProvider>;
  }

  return <UserInitProvider initialUser={user}>{children}</UserInitProvider>;
}
