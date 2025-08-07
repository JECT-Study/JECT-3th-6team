'use client';

import QueryErrorFallback from '@/shared/ui/error/QueryErrorFallback';
import PopupCardListSuspenseFallback from '@/entities/popup/ui/PopupCardListSuspenseFallback';
import FilteredPopupList from '@/features/filtering/ui/FilteredPopupList';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { ErrorBoundary } from 'react-error-boundary';
import { Suspense } from 'react';

export default function FilteredPopupListWrapper() {
  return (
    <QueryErrorResetBoundary>
      {({ reset }) => (
        <ErrorBoundary
          onReset={reset}
          fallbackRender={({ error, resetErrorBoundary }) => (
            <QueryErrorFallback onRetry={resetErrorBoundary} error={error} />
          )}
        >
          <Suspense fallback={<PopupCardListSuspenseFallback />}>
            <FilteredPopupList />
          </Suspense>
        </ErrorBoundary>
      )}
    </QueryErrorResetBoundary>
  );
}
