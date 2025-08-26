export const MIN_NAME_LENGTH = 2;
export const MAX_NAME_LENGTH = 10;
export const MIN_HEAD_COUNT = 1;
export const MAX_HEAD_COUNT = 6; // 1회당 예약할 수 있는 최대 인원수

export const ERROR_CODE_MAP: Record<string, string> = {
  INVALID_EMAIL_FORMAT: '이메일 형식을 확인해주세요!',
  EMPTY_EMAIL: '이메일을 입력해주세요',
  INVALID_NAME_LENGTH: `${MIN_NAME_LENGTH}~${MAX_NAME_LENGTH}글자로 입력해주십시오.`,
  SPECIAL_CHAR_INCLUDED: '특수문자는 포함될 수 없어요.',
  ALERT_MAX_HEADCOUNT: `최대 ${MAX_HEAD_COUNT}명까지 가능해요.`,

  EMPTY_FIELD_EXIST: '모든 항목을 입력해주세요',
  NONE: '',
} as const;
