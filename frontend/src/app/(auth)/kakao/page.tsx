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
    <div className="flex h-[60vh] items-center justify-center">
      <div className="animate-pulse rounded-xl bg-gray-100 px-6 py-4 text-gray-600">
        로그인 처리 중이에요…
      </div>
    </div>
  );
}
