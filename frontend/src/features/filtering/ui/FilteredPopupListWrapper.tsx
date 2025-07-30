'use client';

import FilteredPopupListErrorFallback from '@/features/filtering/ui/FilteredPopupListErrorFallback';
import { Suspense } from 'react';
import PopupCardListSuspenseFallback from '@/entities/popup/ui/PopupCardListSuspenseFallback';
import FilteredPopupList from '@/features/filtering/ui/FilteredPopupList';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { ErrorBoundary } from 'react-error-boundary';

export default function FilteredPopupListWrapper() {
  return (
    <QueryErrorResetBoundary>
      {({ reset }) => (
        <ErrorBoundary
          onReset={reset}
          fallbackRender={({ error, resetErrorBoundary }) => (
            <FilteredPopupListErrorFallback
              onRetry={resetErrorBoundary}
              error={error}
            />
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
