'use client';

import { useQuery } from '@tanstack/react-query';
import getUserApi from '@/entities/user/api/getUserApi';
import { useUserStore } from '@/entities/user/lib/useUserStore';
import { useRouter, useSearchParams } from 'next/navigation';
import { useEffect } from 'react';

export default function Kakao() {
  const {
    data: user,
    isError,
    isSuccess,
  } = useQuery({
    queryKey: ['auth', 'user'],
    queryFn: () => getUserApi(),
    retry: false,
    staleTime: 5 * 60 * 1000, // 5분
    gcTime: 30 * 60 * 1000, // 30분
    refetchOnWindowFocus: false,
    select: data => (data ? { email: data.email, nickname: data.name } : null),
    throwOnError: false,
  });
  const router = useRouter();
  const searchParams = useSearchParams();
  const redirectTo = searchParams.get('redirect_path') || '/';
  const setUser = useUserStore(state => state.setUser);
  const clearUser = useUserStore(state => state.clearUser);
  // ❗ useEffect로 분리: isError 처리
  useEffect(() => {
    if (isError) {
      clearUser();
    }
  }, [isError, clearUser]);

  // ❗ useEffect로 분리: 성공 시 사용자 설정 + 라우터 이동
  useEffect(() => {
    if (user && isSuccess) {
      setUser({
        email: user.email,
        nickname: user.nickname,
        role: 'user',
      });
      router.replace(redirectTo);
    }
  }, [user, isSuccess, setUser, router]);

  return (
    <div className="flex flex-col items-center justify-center h-screen text-gray-600">
      {/* 기본 spin 애니메이션 */}
      <div className="w-8 h-8 mb-4 border-4 border-t-transparent border-blue-500 rounded-full animate-spin" />
      <p className="text-base animate-pulse">로그인 중이이에요</p>
    </div>
  );
}
