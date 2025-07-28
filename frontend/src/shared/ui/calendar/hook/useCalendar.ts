import { useEffect, useState } from 'react';
import { addMonths, isSameMonth, subMonths } from 'date-fns';
import {
  DateRange,
  MonthlyCalendarProps,
} from '@/shared/ui/calendar/MonthlyCalendar';
import { isSameDay } from '@/shared/ui/calendar/lib/calendarUtils';

type TempRangeType = {
  start: Date | null;
  end: Date | null;
};

export default function useCalendar(props: MonthlyCalendarProps) {
  const { mode, selected, onSelect } = props;
  const today = new Date();
  const [tempRange, setTempRange] = useState<TempRangeType | null>({
    ...(selected as DateRange),
  });
  const [browsingDate, setBrowsingDate] = useState(today);

  useEffect(() => {
    if (mode === 'range') setTempRange(selected as DateRange);
  }, [selected, mode]);

  // 선택한 날짜로 Date 변경
  const onChangeDate = (date: Date) => {
    if (mode === 'single') {
      setBrowsingDate(date);
      onSelect(date);
      return;
    }
    // mode=== 'range'
    if (!tempRange || (tempRange.start && tempRange.end)) {
      const next = {
        start: date,
        end: null,
      };
      setTempRange(next);
      onSelect(next as DateRange);
      return;
    } else {
      // 두번째로 클릭된 경우, date와 start를 비교
      const { start } = tempRange;
      const next =
        start! < date
          ? { start: start, end: date }
          : { start: date, end: start };
      setTempRange(next);
      onSelect(next as DateRange);
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
