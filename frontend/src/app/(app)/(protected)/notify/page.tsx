'use client';

import PageHeader from '@/shared/ui/header/PageHeader';
import { ErrorBoundary } from 'react-error-boundary';
import QueryErrorFallback from '@/shared/ui/error/QueryErrorFallback';
import { Suspense } from 'react';
import PopupCardListSuspenseFallback from '@/entities/popup/ui/PopupCardListSuspenseFallback';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import NotificationCardList from '@/features/notification/ui/NotificationCardList';

export default function NotifyPage() {
  return (
    <div>
      <PageHeader title={'알림 내역'} />
      <QueryErrorResetBoundary>
        {({ reset }) => (
          <ErrorBoundary
            onReset={reset}
            fallbackRender={({ error, resetErrorBoundary }) => (
              <QueryErrorFallback onRetry={resetErrorBoundary} error={error} />
            )}
          >
            <Suspense fallback={<PopupCardListSuspenseFallback />}>
              <NotificationCardList />
            </Suspense>
          </ErrorBoundary>
        )}
      </QueryErrorResetBoundary>
    </div>
  );
}
