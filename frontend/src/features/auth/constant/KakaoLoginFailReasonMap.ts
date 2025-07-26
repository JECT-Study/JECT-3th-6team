export const KAKAO_LOGIN_FAIL_REASON_MAP = {
  token_exchange_failed: '로그인 중 문제가 발생했어요.',
  server_error: '스팟잇 서버에서 일시적인 오류가 발생했어요.',
} as const;
export type KakaoLoginFailType = keyof typeof KAKAO_LOGIN_FAIL_REASON_MAP;
