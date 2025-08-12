/**
 * 영어 요일을 한국어 요일로 변환하는 유틸리티 함수
 * @param dayOfWeek - 영어 요일 (MONDAY, TUESDAY, ...)
 * @returns 한국어 요일 (월, 화, ...)
 */
export const convertDayOfWeekToKorean = (dayOfWeek: string): string => {
  const dayOfWeekMap: Record<string, string> = {
    MONDAY: '월',
    TUESDAY: '화',
    WEDNESDAY: '수',
    THURSDAY: '목',
    FRIDAY: '금',
    SATURDAY: '토',
    SUNDAY: '일',
  };

  return dayOfWeekMap[dayOfWeek];
};
