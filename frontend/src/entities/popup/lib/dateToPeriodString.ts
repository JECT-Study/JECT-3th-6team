/**
 * 시작일과 종료일을 "MM월 DD일 ~ MM월 DD일" 형식의 문자열로 변환합니다.
 *
 * 주의: JavaScript의 getMonth()는 0부터 시작하므로, 실제 월을 얻기 위해 +1을 해줍니다.
 *
 * @param startDate - 시작 날짜 객체
 * @param endDate - 종료 날짜 객체
 * @returns 포맷된 기간 문자열 예: "7월 5일 ~ 7월 10일"
 */
export const dateToPeriodKRString = (startDate: Date, endDate: Date) => {
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

export const dateToPeriodString = (startDate: Date, endDate: Date) => {
  const startMonth = startDate.getMonth() + 1;
  const startDay = startDate.getDate();
  const endMonth = endDate.getMonth() + 1;
  const endDay = endDate.getDate();

  return `${startMonth}.${startDay} ~ ${endMonth}.${endDay}`;
};

/**
 *  "2025-06-01 ~ 2025-06-25" 에서 시작날짜, 종료날짜를 추출해서 Date 객체로 변환합니다.
 */
export const periodStringToDate = (periodString: string) => {
  const [startDate, endDate] = periodString.split(' ~ ');
  return {
    startDate: new Date(startDate),
    endDate: new Date(endDate),
  };
};
