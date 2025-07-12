'use client';

import { dateFormatter } from '@/shared/ui/calendar/lib/dateFormatter';
import { getMonthlyCalendarData } from '@/shared/ui/calendar/lib/calendarUtils';
import IconBracketLeft from '@/assets/icons/Normal/Icon_Bracket_Left.svg';
import IconBracketRight from '@/assets/icons/Normal/Icon_Bracket_Right.svg';
import useCalendar from './hook/useCalendar';
import {
  dateCell,
  rangeBlock,
} from '@/shared/ui/calendar/lib/calendarStyleVariants';

export type DateRange = {
  start: Date | null;
  end: Date | null;
};
export interface MonthlyCalendarProps {
  mode: 'single' | 'range';
  selected: Date | DateRange | null;
  onSelected: (value: Date | DateRange) => void;
}

export default function MonthlyCalendar({
  mode,
  selected,
  onSelected,
}: MonthlyCalendarProps) {
  const { browsingDate, prevMonth, nextMonth, onChangeDate, getDateState } =
    useCalendar({
      mode,
      selected,
      onSelected,
    });
  const { currentMonthAllDates, weekDays } =
    getMonthlyCalendarData(browsingDate);
  const { year, month } = dateFormatter(browsingDate);

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
          {currentMonthAllDates.map((date, index) => {
            const state = getDateState(date);

            return (
              <button
                key={index}
                className={`relative w-11 h-11`}
                type="button"
                onClick={() => onChangeDate(date)}
              >
                <span
                  className={rangeBlock({
                    isInRange: state.isInRange,
                    isStartDate: state.isStartDate,
                    isEndDate: state.isEndDate,
                  })}
                ></span>

                <span
                  className={dateCell({
                    isToday: state.isToday,
                    isSelected: state.isSelected,
                    isStartDate: state.isStartDate,
                    isEndDate: state.isEndDate,
                    isCurrentMonth: state.isCurrentMonth,
                  })}
                >
                  {date.getDate()}
                </span>
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
}
