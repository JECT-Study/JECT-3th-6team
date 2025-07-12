import {
  addMonths,
  eachDayOfInterval,
  endOfMonth,
  endOfWeek,
  startOfMonth,
  startOfWeek,
  startOfYear,
} from 'date-fns';

export const getMonthlyCalendarData = (selectedDate: Date) => {
  const weekDays = ['월', '화', '수', '목', '금', '토', '일'];
  const allMonth = [];
  const startMonth = startOfYear(selectedDate);
  for (let month = 0; month < 12; month++) {
    allMonth.push(addMonths(startMonth, month));
  }

  const startDayOfCurrentMonth = startOfMonth(selectedDate); // 현재 달의 시작 날짜
  const endDayOfCurrentMonth = endOfMonth(selectedDate); // 현재달의마지막날짜
  const startDayOfFirstWeek = startOfWeek(startDayOfCurrentMonth, {
    weekStartsOn: 1,
  }); // 현재달의첫주의시작날짜
  const endDayOfLastWeek = endOfWeek(endDayOfCurrentMonth, { weekStartsOn: 1 }); // 현재달마지막주의끝날짜
  const currentMonthAllDates = eachDayOfInterval({
    start: startDayOfFirstWeek,
    end: endDayOfLastWeek,
  });

  return { weekDays, currentMonthAllDates, allMonth };
};

export const isSameMonth = (date1: Date, date2: Date) => {
  return (
    date1.getFullYear() === date2.getFullYear() &&
    date1.getMonth() === date2.getMonth()
  );
};

export const isSameDay = (date1: Date, date2: Date) => {
  return (
    date1.getFullYear() === date2.getFullYear() &&
    date1.getDate() === date2.getDate() &&
    date1.getMonth() === date2.getMonth()
  );
};
