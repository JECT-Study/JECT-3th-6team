'use client';

import {
  BottomButtonContainer,
  ModalContainer,
  NumberInput,
  StandardButton,
  TextInput,
} from '@/shared/ui';
import useForm from '../hook/useForm';
import IconReload from '@/assets/icons/Normal/Icon_Reload.svg';
import {
  ERROR_CODE_MAP,
  MAX_HEAD_COUNT,
  MIN_HEAD_COUNT,
} from '@/features/reservation/model/ErrorCodeMap';
import { useState } from 'react';
import ReservationCheckModal from '@/features/reservation/ui/ReservationCheckModal';

import usePostReservation from '@/features/reservation/hook/usePostReservation';

export default function OnsiteReservationForm({
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

  const { mutate } = usePostReservation({ popupId });

  const headcountError =
    formValue.headCount >= MAX_HEAD_COUNT
      ? ERROR_CODE_MAP.ALERT_MAX_HEADCOUNT
      : ERROR_CODE_MAP.NONE;

  const handleModalOpen = () => {
    setIsOpenModal(true);
  };
  const handleModalClose = () => {
    setIsOpenModal(false);
  };

  const handleSubmit = async () => {
    mutate({
      popupId,
      name: formValue.name,
      peopleCount: formValue.headCount,
      email: formValue.email,
    });
  };

  return (
    <div>
      <div className={'px-5 flex flex-col gap-y-11.5 mt-4'}>
        <TextInput
          inputMode={'text'}
          label={'대기자 명'}
          placeholder={'대기자명을 입력하세요'}
          id={'reservation-name'}
          value={formValue.name}
          onChange={(value: string) => handleChange('name', value)}
          errorMessage={error.name}
          error={error.name !== ''}
        />

        <NumberInput
          label={'대기자 수'}
          value={formValue.headCount}
          max={MAX_HEAD_COUNT}
          min={MIN_HEAD_COUNT}
          onChange={(value: number) => handleChange('headCount', value)}
          errorMessage={headcountError}
        />

        <TextInput
          inputMode={'email'}
          label={'대기자 이메일'}
          placeholder={'user@gmail.com'}
          id={'reservation-email'}
          value={formValue.email}
          onChange={(value: string) => handleChange('email', value)}
          errorMessage={error.email}
          error={error.email !== ''}
        />
      </div>

      <BottomButtonContainer hasShadow={false}>
        <StandardButton
          onClick={handleReset}
          disabled={false}
          size={'fit'}
          color={'white'}
          hasShadow={false}
          className={'rounded-[10px]'}
        >
          <div className={'flex items-center gap-x-2'}>
            <IconReload width={17} height={17} fill={'var(--color-gray60)'} />
            <span>초기화</span>
          </div>
        </StandardButton>

        <StandardButton
          onClick={handleModalOpen}
          disabled={!isFormValid}
          size={'full'}
          color={'primary'}
          hasShadow={false}
        >
          확인
        </StandardButton>
      </BottomButtonContainer>
      <ModalContainer open={isOpenModal}>
        <ReservationCheckModal
          handleModalClose={handleModalClose}
          handleSubmit={handleSubmit}
          data={formValue}
        />
      </ModalContainer>
    </div>
  );
}
