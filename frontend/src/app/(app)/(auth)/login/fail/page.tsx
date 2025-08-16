import {
  KAKAO_LOGIN_FAIL_REASON_MAP,
  KakaoLoginFailType,
} from '@/features/auth/constant/KakaoLoginFailReasonMap';
import Link from 'next/link';

export default async function LoginFailPage({
  searchParams,
}: {
  searchParams: Promise<{ reason: KakaoLoginFailType }>;
}) {
  const { reason } = await searchParams;
  const DEFAULT_FAIL_MESSAGE = '알 수 없는 오류가 발생했어요.';
  const message = KAKAO_LOGIN_FAIL_REASON_MAP[reason] ?? DEFAULT_FAIL_MESSAGE;

  return (
    <div className="min-h-screen flex flex-col items-center justify-center px-4 text-center bg-gray-50">
      {/* 경고 아이콘 (SVG) */}
      <div className="w-12 h-12 mb-4 text-red-500" aria-hidden="true">
        <svg
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          className="w-full h-full"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M12 9v2m0 4h.01M5.07 19h13.86c1.08 0 1.69-1.22 1.12-2.1L13.12 4.9a1.5 1.5 0 00-2.24 0L3.95 16.9A1.25 1.25 0 005.07 19z"
          />
        </svg>
      </div>

      {/* 제목 및 메시지 */}
      <h1 className="text-xl font-semibold text-gray-800 mb-2">로그인 실패</h1>
      <p className="text-gray-600 mb-6">{message}</p>

      {/* 버튼들 */}
      <div className="flex gap-3">
        <Link
          href="/login"
          className="bg-black text-white px-4 py-2 rounded-md text-sm hover:bg-gray-800 transition"
        >
          다시 로그인
        </Link>
        <Link
          href="/frontend/public"
          className="border border-gray-300 text-gray-700 px-4 py-2 rounded-md text-sm hover:bg-gray-100 transition"
        >
          홈으로
        </Link>
      </div>
    </div>
  );
}
