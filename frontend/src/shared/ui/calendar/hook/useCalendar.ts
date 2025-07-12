import { useState } from 'react';
import { addMonths, isSameMonth, subMonths } from 'date-fns';
import {
  DateRange,
  MonthlyCalendarProps,
} from '@/shared/ui/calendar/MonthlyCalendar';
import { isSameDay } from '@/shared/ui/calendar/lib/calendarUtils';

export default function useCalendar({
  mode,
  selected,
  onSelected,
}: MonthlyCalendarProps) {
  const today = new Date();
  const [tempRange, setTempRange] = useState<DateRange | null>(null);
  const [browsingDate, setBrowsingDate] = useState(today);

  // 선택한 날짜로 Date 변경
  const onChangeDate = (date: Date) => {
    if (mode === 'single') {
      setBrowsingDate(date);
      onSelected(date);
      return;
    }
    // mode=== 'range'
    if (!tempRange || (tempRange.start && tempRange.end)) {
      const next = {
        start: date,
        end: null,
      };
      setTempRange(next);
      return;
    } else {
      // 두번째로 클릭된 경우, date와 start를 비교
      const { start } = tempRange;
      const next: DateRange =
        start! < date
          ? { start: start, end: date }
          : { start: date, end: start };
      setTempRange(next);
      onSelected(next);
      return;
    }
  };

  const getDateState = (date: Date) => {
    const isToday = isSameDay(date, today);
    const isCurrentMonth = isSameMonth(browsingDate, date);

    const isInRange =
      mode === 'range' &&
      tempRange !== null &&
      tempRange.start !== null &&
      tempRange.end !== null &&
      date > tempRange.start &&
      date < tempRange.end;

    const isStartDate =
      mode === 'range' &&
      tempRange !== null &&
      'start' in tempRange &&
      tempRange.start !== null &&
      isSameDay(date, tempRange.start);

    const isEndDate =
      mode === 'range' &&
      tempRange !== null &&
      'end' in tempRange &&
      tempRange.end !== null &&
      isSameDay(date, tempRange.end);

    const isSelected =
      mode === 'single' &&
      selected instanceof Date &&
      isSameDay(date, selected);

    return {
      isToday,
      isCurrentMonth,
      isSelected,
      isStartDate,
      isEndDate,
      isInRange,
    };
  };

  return {
    today,
    browsingDate,
    tempRange,
    onChangeDate,
    getDateState,
    nextMonth: () => setBrowsingDate(addMonths(browsingDate, 1)),
    prevMonth: () => setBrowsingDate(subMonths(browsingDate, 1)),
  };
}
