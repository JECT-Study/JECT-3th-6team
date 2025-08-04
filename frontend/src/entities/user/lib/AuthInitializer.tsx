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
    error,
  } = useQuery({
    queryKey: ['auth', 'user'],
    queryFn: getUserApi,
    retry: 1,
    staleTime: 5 * 60 * 1000, // 5분
    gcTime: 30 * 60 * 1000, // 30분
    refetchOnWindowFocus: false,
  });

  if (isLoading) {
    return null;
  }

  if (isError) {
    console.error('유저정보 조회에 실패하였습니다.', error);
  }

  return (
    <UserInitProvider initialUser={user ?? null}>{children}</UserInitProvider>
  );
}
