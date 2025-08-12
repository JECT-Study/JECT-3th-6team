'use client';

import PageHeader from '@/shared/ui/header/PageHeader';
import ReservationDetailWrapper from '@/features/reservation/ui/ReservationDetailWrapper';
import { BottomButtonContainer, StandardButton } from '@/shared/ui';
import React from 'react';
import { useRouter } from 'next/navigation';

export default function ReservationCompletePage() {
  const router = useRouter();
  return (
    <div>
      <PageHeader title={'웨이팅 확정'} />
      <ReservationDetailWrapper />
      <BottomButtonContainer hasShadow={false}>
        <StandardButton
          onClick={() => router.push(`/`)}
          disabled={false}
          color={'primary'}
        >
          내 대기번호 확인하기
        </StandardButton>
      </BottomButtonContainer>
    </div>
  );
}
