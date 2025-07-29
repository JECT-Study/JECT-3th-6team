'use client';
import { BottomButtonContainer, StandardButton } from '@/shared/ui';
import { useRouter } from 'next/navigation';

export default function WaitingBottomButtonContainer({
  waitingId,
}: {
  waitingId: number;
}) {
  const router = useRouter();

  const handlePushReservationDetail = () => {
    router.push(`/reservation/detail/${waitingId}`);
  };
  return (
    <BottomButtonContainer hasShadow={false}>
      <StandardButton
        onClick={handlePushReservationDetail}
        disabled={false}
        color={'white'}
      >
        예약내역 확인
      </StandardButton>
      <StandardButton
        onClick={() => router.back()}
        disabled={false}
        color={'primary'}
      >
        이전으로
      </StandardButton>
    </BottomButtonContainer>
  );
}
