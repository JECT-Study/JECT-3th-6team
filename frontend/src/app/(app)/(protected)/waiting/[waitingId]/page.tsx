import PageHeader from '@/shared/ui/header/PageHeader';
import WaitingBottomButtonContainer from '@/features/waiting/ui/WaitingBottomButtonContainer';
import WaitingCountWrapper from '@/features/waiting/ui/WaitingCountWrapper';

export default async function WaitingPage({
  params,
}: {
  params: Promise<{ waitingId: number }>;
}) {
  const { waitingId } = await params;

  return (
    <div>
      <PageHeader title={'대기 순번'} />
      <WaitingCountWrapper />
      <WaitingBottomButtonContainer waitingId={waitingId} />
    </div>
  );
}
