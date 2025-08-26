import { Suspense } from 'react';
import Kakao from '@/features/auth/ui/Kakao';

export default function Page() {
  return (
    <Suspense fallback={<KakaoLoading />}>
      <Kakao />
    </Suspense>
  );
}

function KakaoLoading() {
  return (
    <div className="flex flex-col items-center justify-center h-screen text-gray-600">
      {/* 기본 spin 애니메이션 */}
      <div className="w-8 h-8 mb-4 border-4 border-t-transparent border-blue-500 rounded-full animate-spin" />
      <p className="text-base animate-pulse">로그인 중이이에요</p>
    </div>
  );
}
