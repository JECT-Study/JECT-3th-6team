'use client';

import { FilterProvider } from '@/features/filtering/lib/FilterContext';
import FilterBottomSheet from '@/features/filtering/ui/FilterBottomSheet';
import FilterGroupMapContainer from '@/features/map/ui/FilterGroupMapContainer';
import { Suspense } from 'react';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import { ErrorBoundary } from 'react-error-boundary';
import QueryErrorFallback from '@/shared/ui/error/QueryErrorFallback';
import LoadingFallback from '@/shared/ui/loading/LoadingFallback';

export default function MapPage() {
  return (
    <Suspense fallback={<LoadingFallback />}>
      <FilterProvider>
        <QueryErrorResetBoundary>
          {({ reset }) => (
            <ErrorBoundary
              onReset={reset}
              fallbackRender={({ error, resetErrorBoundary }) => (
                <QueryErrorFallback
                  onRetry={resetErrorBoundary}
                  error={error}
                />
              )}
            >
              <Suspense fallback={<LoadingFallback />}>
                <FilterGroupMapContainer />
              </Suspense>
            </ErrorBoundary>
          )}
        </QueryErrorResetBoundary>
        <FilterBottomSheet />
      </FilterProvider>
    </Suspense>
  );
}
