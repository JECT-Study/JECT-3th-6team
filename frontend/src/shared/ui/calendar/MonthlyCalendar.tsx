'use client';

import Image from 'next/image';
import { useState } from 'react';
import { addMonths, subMonths } from 'date-fns';
import makeCalender from '@/shared/ui/calendar/lib/makeCalendar';
import { dateFormatter } from '@/shared/ui/calendar/lib/dateFormatter';
import IconBracketLeft from '/public/icons/Normal/Icon_Bracket_Left.svg';
import IconBracketRight from '/public/icons/Normal/Icon_Bracket_Right.svg';

interface MonthlyCalendarProps {
  selectedDate: Date;
  onClick: (date: Date) => void;
}

export default function MonthlyCalendar({
  selectedDate,
  onClick,
}: MonthlyCalendarProps) {
  const [browsingDate, setBrowsingDate] = useState(selectedDate);
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
    setBrowsingDate(date);
    onClick(date);
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

  return (
    <div className="w-full py-6 bg-white rounded-[14px]  border-[1.2px] border-gray40 shadow-calendar">
      <div className="flex flex-col ">
        {/*년/월/좌우버튼*/}
        <div className="flex items-center justify-between px-6 mb-3">
          <div className="w-full flex justify-between items-center">
            <span className="text-black text-xl font-semibold">{`${year}년 ${month}월`}</span>
            <div className={'flex gap-x-6'}>
              <button
                type="button"
                onClick={prevMonth}
                className={
                  'w-8 h-8 flex items-center justify-center cursor-pointer border border-gray20 rounded-lg bg-gray10'
                }
              >
                <Image
                  src={IconBracketLeft}
                  alt="left"
                  width={18}
                  height={18}
                />
              </button>
              <button
                type="button"
                onClick={nextMonth}
                className={
                  'w-8 h-8 flex items-center justify-center cursor-pointer border border-gray20 rounded-lg bg-gray10'
                }
              >
                <Image
                  src={IconBracketRight}
                  alt="left"
                  width={18}
                  height={18}
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
              className="px-3.5 py-4 flex items-center justify-center font-regular text-gray60 text-sm"
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
              className={`w-11 h-11 rounded-full font-medium text-base 
              ${isSameMonth(browsingDate, date) ? 'text-black' : 'text-gray40'}
              ${isSameDay(selectedDate, date) ? 'bg-main  font-semibold text-white' : ''}`}
              type="button"
              onClick={() => onChangeDate(date)}
            >
              {date.getDate()}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
