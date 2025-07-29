interface ErrorFallbackProps {
  onRetry?: () => void;
}

export default function FilteredPopupListErrorFallback({
  onRetry,
}: ErrorFallbackProps) {
  return (
    <div className="flex flex-col items-center justify-center text-center py-16 px-4 text-gray-500 bg-white">
      <p className="text-lg font-semibold">문제가 발생했어요</p>
      <p className="mt-2 text-sm text-gray-500">
        데이터를 불러오는 중 오류가 발생했어요.
      </p>
      {onRetry && (
        <button
          onClick={onRetry}
          className="mt-4 px-4 py-2 text-sm font-medium bg-red-500 text-white rounded-md hover:bg-red-600"
        >
          다시 시도하기
        </button>
      )}
    </div>
  );
}
