'use client';

import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { ErrorBoundary } from 'react-error-boundary';
import QueryErrorFallback from '@/shared/ui/error/QueryErrorFallback';
import { Suspense } from 'react';
import LoadingFallback from '@/shared/ui/loading/LoadingFallback';
import ReservationDetail from '@/features/reservation/ui/ReservationDetail';

export default function ReservationDetailWrapper() {
  return (
    <div>
      <QueryErrorResetBoundary>
        {({ reset }) => (
          <ErrorBoundary
            onReset={reset}
            fallbackRender={({ error, resetErrorBoundary }) => (
              <QueryErrorFallback onRetry={resetErrorBoundary} error={error} />
            )}
          >
            <Suspense fallback={<LoadingFallback />}>
              <ReservationDetail />
            </Suspense>
          </ErrorBoundary>
        )}
      </QueryErrorResetBoundary>
    </div>
  );
}
