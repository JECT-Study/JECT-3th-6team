'use client';

import { useParams } from 'next/navigation';
import { Suspense } from 'react';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { ErrorBoundary } from 'react-error-boundary';

import LoadingFallback from '@/shared/ui/loading/LoadingFallback';
import QueryErrorFallback from '@/shared/ui/error/QueryErrorFallback';
import PopupDetailContainer from '@/features/detail/ui/PopupDetailContainer';

export default function ProductDetail() {
  const params = useParams();
  const popupId = Number(params.popupId);

  return (
    <QueryErrorResetBoundary>
      {({ reset }) => (
        <ErrorBoundary
          onReset={reset}
          fallbackRender={({ error, resetErrorBoundary }) => (
            <QueryErrorFallback onRetry={resetErrorBoundary} error={error} />
          )}
        >
          <Suspense fallback={<LoadingFallback />}>
            <PopupDetailContainer popupId={popupId} />
          </Suspense>
        </ErrorBoundary>
      )}
    </QueryErrorResetBoundary>
  );
}
