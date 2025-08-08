'use client';

import { ErrorBoundary } from 'react-error-boundary';
import QueryErrorFallback from '@/shared/ui/error/QueryErrorFallback';
import { Suspense } from 'react';
import WaitingCount from '@/features/waiting/ui/WaitingCount';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import LoadingFallback from '@/shared/ui/loading/LoadingFallback';

export default function WaitingCountWrapper() {
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
              <WaitingCount />
            </Suspense>
          </ErrorBoundary>
        )}
      </QueryErrorResetBoundary>
    </div>
  );
}
