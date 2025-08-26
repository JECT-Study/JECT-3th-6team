import { ERROR_CODE_MAP } from '@/features/reservation/model/ErrorCodeMap';
import { OnsiteReservationFormValue } from '@/features/reservation/hook/useForm';

type ValidateResultType = {
  isValid: boolean;
  errorCode: keyof typeof ERROR_CODE_MAP;
};

export class FormValidate {
  private static readonly EMAIL_REGEX =
    /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
  private static readonly NO_SPECIAL_CHAR_REGEX = /^[가-힣a-zA-Z0-9]+$/;

  static trimFormValue(value: string) {
    return value.trim();
  }

  static validateEmail(email: string): ValidateResultType {
    if (email.length === 0) {
      return {
        isValid: false,
        errorCode: 'EMPTY_EMAIL',
      };
    }

    if (!this.EMAIL_REGEX.test(email)) {
      return {
        isValid: false,
        errorCode: 'INVALID_EMAIL_FORMAT',
      };
    }
    return {
      isValid: true,
      errorCode: 'NONE',
    };
  }

  static validateName(name: string): ValidateResultType {
    if (name.length > 10 || name.length < 2) {
      return {
        isValid: false,
        errorCode: 'INVALID_NAME_LENGTH',
      };
    }

    if (!this.NO_SPECIAL_CHAR_REGEX.test(name)) {
      return {
        isValid: false,
        errorCode: 'SPECIAL_CHAR_INCLUDED',
      };
    }

    return {
      isValid: true,
      errorCode: 'NONE',
    };
  }

  static validateOnsiteReservation(formValue: OnsiteReservationFormValue) {
    const { name, email } = formValue;

    if (name.length === 0 || email.length === 0) {
      return {
        isValid: false,
        errorCode: 'EMPTY_FIELD_EXIST',
      };
    }

    // 모든 필드가 유효성 검증을 완료했는지 확인
    const nameValidation = this.validateName(name);
    if (!nameValidation.isValid) {
      return nameValidation;
    }

    const emailValidation = this.validateEmail(email);
    if (!emailValidation.isValid) {
      return emailValidation;
    }

    return {
      isValid: true,
      errorCode: 'NONE',
    };
  }
}
