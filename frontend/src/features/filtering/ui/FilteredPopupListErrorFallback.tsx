import { ApiError } from '@/shared/type/api';

export default function FilteredPopupListErrorFallback({
  onRetry,
  error,
}: {
  onRetry?: () => void;
  error?: Error | ApiError;
}) {
  return (
    <div className="h-[calc(100vh-400px)] flex flex-col items-center justify-center text-center px-4 text-gray-500 bg-white">
      {error?.message && (
        <p className="text-lg font-semibold">{error?.message}</p>
      )}
      <p className="mt-2 text-sm text-gray-500">
        데이터를 불러오는 중 오류가 발생했어요.
      </p>
      {onRetry && (
        <button
          onClick={() => onRetry()}
          className="mt-4 px-4 py-2 text-sm font-medium bg-red-500 text-white rounded-md hover:bg-red-600"
        >
          다시 시도하기
        </button>
      )}
    </div>
  );
}
