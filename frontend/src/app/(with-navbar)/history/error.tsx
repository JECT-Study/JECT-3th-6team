'use client';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { startTransition } from 'react';

interface ErrorFallbackProps {
  error: Error;
  reset?: () => void;
}
export default function Error({ error, reset }: ErrorFallbackProps) {
  const router = useRouter();

  return (
    <div className="w-full min-h-[60vh] flex flex-col items-center justify-center px-6 text-center">
      <h2 className="text-[22px] font-bold text-black mb-2">
        앗! 문제가 발생했어요
      </h2>
      <p className="text-[16px] text-gray-500 mb-6 leading-relaxed">
        잠시 후 다시 시도해주세요.
        <br />
        문제가 계속되면 고객센터로 문의해주세요.
      </p>
      <div className="flex gap-2">
        {reset && (
          <button
            onClick={() => {
              startTransition(() => {
                router.refresh();
                reset();
              });
            }}
            className="px-4 py-2 rounded-[8px] bg-[#3182F6] text-white text-[15px] font-medium hover:bg-[#1A5EFF] transition"
          >
            다시 시도하기
          </button>
        )}
        <Link
          href={'/'}
          className="px-4 py-2 rounded-[8px] bg-[#F2F3F6] text-[15px] text-gray-700 font-medium hover:bg-[#E5E8EB] transition"
        >
          홈으로 가기
        </Link>
      </div>
      <p className="text-[13px] text-gray-400 mt-4">{error.message}</p>
    </div>
  );
}
