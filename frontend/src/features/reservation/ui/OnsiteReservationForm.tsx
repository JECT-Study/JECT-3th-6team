'use client';

import {
  BottomButtonContainer,
  NumberInput,
  StandardButton,
  TextInput,
} from '@/shared/ui';

import IconReload from '@/assets/icons/Normal/Icon_Reload.svg';
import {
  ERROR_CODE_MAP,
  MAX_HEAD_COUNT,
  MIN_HEAD_COUNT,
} from '@/features/reservation/model/ErrorCodeMap';
import {
  OnsiteReservationFormError,
  OnsiteReservationFormValue,
} from '@/features/reservation/hook/useForm';

interface OnsiteReservationFormProps {
  formValue: OnsiteReservationFormValue;
  error: OnsiteReservationFormError;
  handleChange: (
    key: keyof OnsiteReservationFormValue,
    value: string | number
  ) => void;
  handleReset: () => void;
  isFormValid: boolean;
  handleCheck: () => void;
}

export default function OnsiteReservationForm({
  formValue,
  error,
  handleChange,
  handleReset,
  isFormValid,
  handleCheck,
}: OnsiteReservationFormProps) {
  const headcountError =
    formValue.headCount >= MAX_HEAD_COUNT
      ? ERROR_CODE_MAP.ALERT_MAX_HEADCOUNT
      : ERROR_CODE_MAP.NONE;

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
          onClick={handleCheck}
          disabled={!isFormValid}
          size={'full'}
          color={'primary'}
          hasShadow={false}
        >
          확인
        </StandardButton>
      </BottomButtonContainer>
    </div>
  );
}
