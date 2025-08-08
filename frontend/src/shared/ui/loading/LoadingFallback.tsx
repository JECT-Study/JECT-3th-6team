'use client';

import { cn } from '@/lib/utils'; // 선택적: className 병합 유틸
import { HTMLAttributes } from 'react';

export default function LoadingFallback({
  className,
  ...rest
}: HTMLAttributes<HTMLDivElement>) {
  return (
    <div
      className={cn(
        'flex flex-col items-center justify-center w-full h-full py-10 text-gray-500 dark:text-gray-400',
        className
      )}
      {...rest}
    >
      <div className="w-6 h-6 mb-2 border-4 border-t-transparent border-gray-400 rounded-full animate-spin" />
      <p className="text-sm">로딩 중이에요...</p>
    </div>
  );
}
