'use client';

import { OnsiteReservationForm } from '@/features/reservation';
import { useReservationFormContext } from '@/features/reservation/lib/FormProvider';
import { usePathname, useRouter } from 'next/navigation';

export default function OnsiteReservationContainer() {
  const { formValue, error, handleChange, handleReset, isFormValid } =
    useReservationFormContext();
  const router = useRouter();
  const pathname = usePathname();

  return (
    <div>
      <OnsiteReservationForm
        error={error}
        handleChange={handleChange}
        handleReset={handleReset}
        isFormValid={isFormValid}
        handleCheck={() => router.push(`${pathname}/check`)}
        formValue={formValue}
      />
    </div>
  );
}
