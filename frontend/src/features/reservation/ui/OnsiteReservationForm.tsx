'use client';

import {
  BottomButtonContainer,
  NumberInput,
  StandardButton,
  TextInput,
} from '@/shared/ui';
import useForm from '../hook/useForm';
import Image from 'next/image';
import ReloadIcon from '/public/icons/Normal/Icon_Reload.svg';
import {
  ERROR_CODE_MAP,
  MAX_HEAD_COUNT,
  MIN_HEAD_COUNT,
} from '@/features/reservation/model/ErrorCodeMap';

export default function OnsiteReservationForm() {
  const { formValue, error, handleChange, handleReset } = useForm({
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

  const headcountError =
    formValue.headCount >= MAX_HEAD_COUNT
      ? ERROR_CODE_MAP.ALERT_MAX_HEADCOUNT
      : ERROR_CODE_MAP.NONE;

  return (
    <div>
      <div className={'px-5 flex flex-col gap-y-11.5 mt-4'}>
        <TextInput
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
          label={'대기자 이메일'}
          placeholder={'user@gmail.com'}
          id={'reservation-email'}
          value={formValue.email}
          onChange={(value: string) => handleChange('email', value)}
          errorMessage={error.email}
          error={error.email !== ''}
        />
      </div>

      <BottomButtonContainer>
        <StandardButton
          onClick={handleReset}
          disabled={false}
          size={'fit'}
          color={'white'}
          hasShadow={false}
          customClass={'rounded-lg'}
        >
          <div className={'flex items-center gap-x-2'}>
            <Image
              src={ReloadIcon}
              width={17}
              height={17}
              alt={'reload'}
              objectFit={'cover'}
            />
            <span>초기화</span>
          </div>
        </StandardButton>

        <StandardButton
          onClick={() => console.log('예약!')}
          disabled={false}
          size={'full'}
          color={'primary'}
          hasShadow={false}
        >
          적용하기
        </StandardButton>
      </BottomButtonContainer>
    </div>
  );
}
