import { ReservationFormProvider } from '@/features/reservation/lib/FormProvider';

export default function ReservationOnsiteLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return <ReservationFormProvider>{children}</ReservationFormProvider>;
}
