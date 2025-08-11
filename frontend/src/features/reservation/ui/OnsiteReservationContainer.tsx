'use client';

import { OnsiteReservationForm } from '@/features/reservation';
import ReservationCheckModal from '@/features/reservation/ui/ReservationCheckModal';
import { useState } from 'react';
import usePostReservation from '@/features/reservation/hook/usePostReservation';
import useForm from '@/features/reservation/hook/useForm';

export default function OnsiteReservationContainer({
  popupId,
}: {
  popupId: number;
}) {
  const { formValue, error, handleChange, handleReset, isFormValid } = useForm({
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
  });
  const [isOpenModal, setIsOpenModal] = useState(false);
  const { mutate, isPending } = usePostReservation();

  const handleModalOpen = () => {
    setIsOpenModal(true);
  };
  const handleModalClose = () => {
    setIsOpenModal(false);
  };

  const handleSubmit = async () => {
    if (isPending) return; // 요청 중이면 중복 호출 방지
    mutate({
      popupId,
      name: formValue.name,
      peopleCount: formValue.headCount,
      email: formValue.email,
    });
  };
  return (
    <div>
      <OnsiteReservationForm
        error={error}
        handleChange={handleChange}
        handleReset={handleReset}
        isFormValid={isFormValid}
        handleModalOpen={handleModalOpen}
        formValue={formValue}
      />
      <ReservationCheckModal
        isOpenModal={isOpenModal}
        handleModalClose={handleModalClose}
        handleSubmit={handleSubmit}
        data={formValue}
        isPending={isPending}
      />
    </div>
  );
}
