import { ReservationFormProvider } from '@/features/reservation/lib/FormProvider';

export default async function ReservationOnsiteLayout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: { popupId: number };
}) {
  return (
    <ReservationFormProvider popupId={params.popupId}>
      {children}
    </ReservationFormProvider>
  );
}
