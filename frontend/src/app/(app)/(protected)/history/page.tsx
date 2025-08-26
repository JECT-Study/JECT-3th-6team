'use client';

import PopupHistoryList from '@/features/history/ui/PopupHistoryList';
import { Suspense } from 'react';
import PopupCardListSuspenseFallback from '@/entities/popup/ui/PopupCardListSuspenseFallback';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { ErrorBoundary } from 'react-error-boundary';
import QueryErrorFallback from '@/shared/ui/error/QueryErrorFallback';

export default function HistoryPage() {
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
            <Suspense fallback={<PopupCardListSuspenseFallback />}>
              <PopupHistoryList />
            </Suspense>
          </ErrorBoundary>
        )}
      </QueryErrorResetBoundary>
    </div>
  );
}
