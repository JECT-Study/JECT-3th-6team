'use client';

import { useEffect, useState } from 'react';
import { useDebounce } from '@/shared/lib';
import { FormValidate } from '@/features/reservation/model/FormValidate';
import { ERROR_CODE_MAP } from '@/features/reservation/model/ErrorCodeMap';
import { DEBOUNCE_DELAY_MS } from '@/shared/constant';
import { storage } from '@/shared/lib/localStorage';

type FormType = 'onsite-reservation';

type FormValueMap = {
  'onsite-reservation': OnsiteReservationFormValue;
};

type FormErrorMap = {
  'onsite-reservation': OnsiteReservationFormError;
};

export type OnsiteReservationFormValue = {
  name: string;
  headCount: number;
  email: string;
};

export type OnsiteReservationFormError = {
  name: string;
  headCount: string;
  email: string;
};

export interface UseFormProps<T extends FormType> {
  formType: T;
  initialFormValue: FormValueMap[T];
  initialError: FormErrorMap[T];
  formKey?: string | number; // 각 폼을 구분하는 변수
}

export default function useForm<T extends FormType>({
  formType,
  initialFormValue,
  initialError,
  formKey,
}: UseFormProps<T>) {
  const storageKey = `${formType}-${formKey}`;
  const [formValue, setFormValue] = useState<FormValueMap[T]>(initialFormValue);
  const [error, setError] = useState<FormErrorMap[T]>(initialError);
  const [currentValue, setCurrentValue] = useState<string | number>('');
  const [currentField, setCurrentField] = useState<
    keyof FormValueMap[T] | null
  >(null);
  const [isFormValid, setIsFormValid] = useState(false);
  const [isInitialized, setIsInitialized] = useState(false);

  const validateField = (
    field: keyof FormValueMap[T],
    value: string | number
  ) => {
    switch (field) {
      case 'email':
        const validateResult = FormValidate.validateEmail(value as string);
        return ERROR_CODE_MAP[validateResult.errorCode];

      case 'name':
        const validateNameResult = FormValidate.validateName(value as string);
        return ERROR_CODE_MAP[validateNameResult.errorCode];
    }
  };

  const debouncedValue = useDebounce(currentValue, DEBOUNCE_DELAY_MS.formInput);

  useEffect(() => {
    if (currentField && debouncedValue) {
      const error = validateField(currentField, debouncedValue);
      if (error) {
        setError(prev => ({ ...prev, [currentField]: error }));
      } else {
        setError(prev => ({ ...prev, [currentField]: '' }));
      }
    }
  }, [debouncedValue, currentField]);

  // 폼 복원
  useEffect(() => {
    const saved = localStorage.getItem(storageKey);
    if (saved) {
      try {
        const parsed = JSON.parse(saved);
        setFormValue(parsed);
      } catch (e) {
        console.error('폼 복원 실패', e);
      }
    }
    setIsInitialized(true); // ✅ 복원 완료
  }, [storageKey]);

  // 폼 자동저장
  useEffect(() => {
    if (!isInitialized) return;
    storage.setJSON(storageKey, formValue);
  }, [formValue, storageKey]);

  const handleChange = (
    field: keyof FormValueMap[T],
    value: string | number
  ) => {
    let trimmedValue = value;

    if (typeof value === 'string') {
      trimmedValue = FormValidate.trimFormValue(value as string);
    }

    // 폼 데이터 업데이트
    setFormValue(prev => ({ ...prev, [field]: trimmedValue }));

    setCurrentField(field);
    setCurrentValue(trimmedValue);
  };

  const handleReset = () => {
    setFormValue(initialFormValue);
    setError(initialError);
    storage.clear(storageKey);
  };

  // 폼 전체 유효성 검증,
  const validateForm = () => {
    switch (formType) {
      case 'onsite-reservation':
        const error = FormValidate.validateOnsiteReservation(formValue);
        return {
          isValid: error.isValid,
          errorMessage: ERROR_CODE_MAP[error.errorCode],
        };
    }
  };

  useEffect(() => {
    const { isValid } = validateForm();
    setIsFormValid(isValid);
  }, [formValue, error]);
  return {
    formValue,
    error,
    handleChange,
    validateField,
    handleReset,
    validateForm,
    isFormValid,
  };
}
