'use client';

import { useState } from 'react';
import { addMonths, subMonths } from 'date-fns';
import makeCalender from '@/shared/ui/calendar/lib/makeCalendar';
import { dateFormatter } from '@/shared/ui/calendar/lib/dateFormatter';
import IconBracketLeft from '@/assets/icons/Normal/Icon_Bracket_Left.svg';
import IconBracketRight from '@/assets/icons/Normal/Icon_Bracket_Right.svg';
import { cn } from '@/lib/utils';

type DateRange = {
  start: Date | null;
  end: Date | null;
};
interface MonthlyCalendarProps {
  mode: 'single' | 'range';
  selected: Date | DateRange | null;
  onSelected: (value: Date | DateRange) => void;
}

export default function MonthlyCalendar({
  mode,
  selected,
  onSelected,
}: MonthlyCalendarProps) {
  const [tempRange, setTempRange] = useState<DateRange | null>(null);
  const today = new Date();
  const [browsingDate, setBrowsingDate] = useState(today);
  const { currentMonthAllDates, weekDays } = makeCalender(browsingDate);
  const { year, month } = dateFormatter(browsingDate);

  // 다음 달로 이동
  const nextMonth = () => {
    setBrowsingDate(addMonths(browsingDate, 1));
  };

  // 지난 달로 이동
  const prevMonth = () => {
    setBrowsingDate(subMonths(browsingDate, 1));
  };

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

  const isSameMonth = (date1: Date, date2: Date) => {
    return (
      date1.getFullYear() === date2.getFullYear() &&
      date1.getMonth() === date2.getMonth()
    );
  };

  const isSameDay = (date1: Date, date2: Date) => {
    return (
      date1.getFullYear() === date2.getFullYear() &&
      date1.getDate() === date2.getDate() &&
      date1.getMonth() === date2.getMonth()
    );
  };

  const isInRange = (date: Date) => {
    if (
      mode !== 'range' ||
      !tempRange ||
      !('start' in tempRange) ||
      !tempRange.start ||
      !tempRange.end
    ) {
      return false;
    }
    return date > tempRange.start && date < tempRange.end;
  };

  const isStartDate = (date: Date) => {
    return (
      mode === 'range' &&
      tempRange &&
      'start' in tempRange &&
      tempRange.start &&
      isSameDay(date, tempRange.start)
    );
  };

  const isEndDate = (date: Date) => {
    return (
      mode === 'range' &&
      tempRange &&
      'end' in tempRange &&
      tempRange.end &&
      isSameDay(date, tempRange.end)
    );
  };

  const isSelected = (date: Date) => {
    return (
      mode === 'single' && selected instanceof Date && isSameDay(date, selected)
    );
  };
  console.log('tempRange', tempRange);
  console.log('selected', selected);

  return (
    <div className="w-full py-6 bg-white rounded-[14px]">
      <div className="flex flex-col ">
        {/*년/월/좌우버튼*/}
        <div className="flex items-center justify-between px-6 mb-3">
          <div className="w-full flex justify-between items-center">
            <span className="text-black text-base font-semibold">{`${year}년 ${month}월`}</span>
            <div className={'flex gap-x-6'}>
              <button
                type="button"
                onClick={prevMonth}
                className={
                  'w-8 h-8 flex items-center justify-center cursor-pointer border border-sub rounded-lg bg-gray10'
                }
              >
                <IconBracketLeft
                  width={20}
                  height={20}
                  fill={'var(--color-main)'}
                />
              </button>
              <button
                type="button"
                onClick={nextMonth}
                className={
                  'w-8 h-8 flex items-center justify-center cursor-pointer border border-sub rounded-lg bg-gray10'
                }
              >
                <IconBracketRight
                  width={20}
                  height={20}
                  fill={'var(--color-main)'}
                />
              </button>
            </div>
          </div>
        </div>
        {/*요일 */}
        <div className="grid grid-cols-7 place-items-center px-3.5">
          {weekDays.map((days, index) => (
            <div
              key={index}
              className="px-3.5 py-4 flex items-center justify-center font-regular text-gray80 text-sm"
            >
              {days}
            </div>
          ))}
        </div>
        {/*날짜 */}

        <div className="grid grid-cols-7 place-items-center px-4">
          {currentMonthAllDates.map((date, index) => (
            <button
              key={index}
              className={cn(
                `relative w-11 h-11 
              `
              )}
              type="button"
              onClick={() => onChangeDate(date)}
            >
              <span
                className={cn(
                  'absolute -inset-x-2',
                  isInRange(date) && ' inset-y-2 block bg-sub2 z-0',
                  isStartDate(date) &&
                    tempRange?.end &&
                    ' left-1/2 -right-1 inset-y-2 block bg-sub2 z-0',
                  isEndDate(date) &&
                    ' right-1/2 -left-1 inset-y-2 block bg-sub2 z-0'
                )}
              ></span>

              <span
                className={cn(
                  'relative font-regular text-[20px] z-20 w-[44px] h-[44px]  flex justify-center items-center ',
                  isSameMonth(browsingDate, date)
                    ? 'text-black'
                    : 'text-gray60',
                  isSameDay(today, date) && 'text-main font-semibold',
                  isSelected(date) &&
                    'bg-main rounded-full font-semibold text-white',
                  isStartDate(date) &&
                    'bg-main rounded-full font-semibold text-white',
                  isEndDate(date) &&
                    'bg-main rounded-full font-semibold text-white',
                  isStartDate(date) &&
                    isSameDay(today, date) &&
                    'text-main bg-main-pale',
                  isEndDate(date) &&
                    isSameDay(today, date) &&
                    'text-main bg-main-pale',
                  isSelected(date) &&
                    isSameDay(today, date) &&
                    'text-main bg-main-pale'
                )}
              >
                {date.getDate()}
              </span>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
