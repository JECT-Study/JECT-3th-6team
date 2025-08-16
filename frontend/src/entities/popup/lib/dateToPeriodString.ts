export const SEP_RE = /\s*~\s*/; // 틸드 주변 공백 유연하게
const DATE_RE = /^(\d{4})[.\-\/](\d{1,2})[.\-\/](\d{1,2})$/; // YYYY.MM.DD / YYYY-MM-DD / YYYY/MM/DD

/**
 * 시작일과 종료일을 "MM월 DD일 ~ MM월 DD일" 형식의 문자열로 변환합니다.
 *
 * 주의: JavaScript의 getMonth()는 0부터 시작하므로, 실제 월을 얻기 위해 +1을 해줍니다.
 *
 * @param startDate - 시작 날짜 객체
 * @param endDate - 종료 날짜 객체
 * @returns 포맷된 기간 문자열 예: "7월 5일 ~ 7월 10일"
 */
export const dateToPeriodKRString = (
  startDate: Date | null,
  endDate: Date | null
) => {
  if (
    !startDate ||
    !endDate ||
    Number.isNaN(startDate.getTime()) ||
    Number.isNaN(endDate.getTime())
  ) {
    return '기간 미정';
  }

  const startMonth = startDate.getMonth() + 1;
  const startDay = startDate.getDate();
  const endMonth = endDate.getMonth() + 1;
  const endDay = endDate.getDate();

  return `${startMonth}월 ${startDay}일 ~ ${endMonth}월 ${endDay}일`;
};

// 시작일과 종료일을 "MM.DD ~ MM.DD" 형식의 문자열로 변환합니다.
/*
 * @param startDate - 시작 날짜 객체
 * @param endDate - 종료 날짜 객체
 * @returns 포맷된 기간 문자열 예: "7.5 ~ 7.10"
 */

export const dateToPeriodString = (startDate: Date | null, endDate: Date) => {
  const startMonth = startDate!.getMonth() + 1;
  const startDay = startDate?.getDate();
  const endMonth = endDate.getMonth() + 1;
  const endDay = endDate.getDate();

  return `${startMonth}.${startDay} ~ ${endMonth}.${endDay}`;
};

/**
 *  "2025-06-01 ~ 2025-06-25" 에서 시작날짜, 종료날짜를 추출해서 Date 객체로 변환합니다.
 */
export const periodStringToDate = (period: string) => {
  if (!period) return { startDate: null, endDate: null };
  const [startRaw, endRaw] = period.split(SEP_RE);
  return {
    startDate: parseDateLoose(startRaw),
    endDate: parseDateLoose(endRaw),
  };
};

/**
 * 날짜 문자열을 Safari 호환 방식으로 느슨하게 파싱합니다.
 * `YYYY.MM.DD` / `YYYY-MM-DD` / `YYYY/MM/DD`를 우선 직접 파싱하고,
 * 아니면 `Date.parse()`로 최종 시도합니다. (로컬 타임존 기준)
 * 월(1–12), 일(1–31)만 1차 검증하며 실패 시 `null`을 반환합니다.
 *
 * @param {string} [dateString] 파싱할 날짜 문자열
 * @returns {Date|null} 유효한 `Date` 또는 `null`
 */
export function parseDateLoose(dateString?: string): Date | null {
  if (!dateString) return null;
  const trimmedDateString = dateString.trim();

  // 명시 파싱 (사파리 호환)
  const dateMatch = trimmedDateString.match(DATE_RE);
  if (dateMatch) {
    const year = Number(dateMatch[1]);
    const month = Number(dateMatch[2]);
    const day = Number(dateMatch[3]);

    // 월일 기본 범위 체크
    if (month < 1 || month > 12 || day < 1 || day > 31) return null;

    return new Date(year, month - 1, day);
  }

  // ISO 등 다른 형식 최종 시도
  const timestamp = Date.parse(trimmedDateString);
  return Number.isNaN(timestamp) ? null : new Date(timestamp);
}
