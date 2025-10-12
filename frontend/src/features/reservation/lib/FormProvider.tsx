'use client';

import useForm, { UseFormProps } from '@/features/reservation/hook/useForm';
import { createContext, useContext } from 'react';

export type ReservationFormContextType = ReturnType<typeof useForm> | null;

export const ReservationFormContext =
  createContext<ReservationFormContextType>(null);

export const useReservationFormContext = () => {
  const context = useContext(ReservationFormContext);
  if (!context) throw new Error('FormContext가 정의되지 않았습니다.');
  return context;
};

export const ReservationFormProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const initialForm: UseFormProps<'onsite-reservation'> = {
    formType: 'onsite-reservation',
    initialFormValue: {
      name: '',
      headCount: 1,
      email: '',
    },
    initialError: {
      name: '',
      headCount: '',
      email: '',
    },
  };
  const form = useForm(initialForm);
  return (
    <ReservationFormContext.Provider value={form}>
      {children}
    </ReservationFormContext.Provider>
  );
};
