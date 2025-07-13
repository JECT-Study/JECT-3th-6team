'use client';

import { useEffect, useState } from 'react';
import { addDays } from 'date-fns';
import { DateRange } from '@/shared/ui/calendar/MonthlyCalendar';

export type FilterType = 'date' | 'keyword' | 'location';
export type KeywordType = {
  popupType: Array<string>;
  category: Array<string>;
};

type FilterValueMap = {
  date: DateRange;
  keyword: KeywordType;
  location: string;
};

type FilterState = {
  [K in FilterType]: FilterValueMap[K];
};
export type FilterValue<T extends FilterType> = FilterValueMap[T];

// TODO InitialFilter를 사용처에서 받는 것으로 변경
const today = new Date();
const aWeekLater = addDays(today, 7);
const initialFilter: FilterState = {
  date: {
    start: today,
    end: aWeekLater,
  },
  location: '전국',
  keyword: {
    popupType: [],
    category: [],
  },
};

export default function useFilter() {
  const [filter, setFilter] = useState<FilterState>(initialFilter); // 최종 필터 상태, 해당 값이 바뀌었을 시 api 호출
  const [tempState, setTempState] = useState<FilterState>(filter); // 바텀 시트 내부 UI 상태 관리용, 적용 버튼을 눌러야 filter에 반영
  const [openType, setOpenType] = useState<FilterType | null>(null); // 바텀 시트 안의 내용물, 어떤 필터가 선택중인지.
  const [isOpen, setIsOpen] = useState(false); // 바텀 시트 open, close 조절
  const [isApplyDisabled, setIsApplyDisabled] = useState(false); // 적용 버튼의 disabled 상태 관리

  const handleOpen = (type: FilterType) => {
    setOpenType(type);
    setIsOpen(true);
  };

  const handleClose = () => {
    setOpenType(null);
    setIsOpen(false);
  };

  // ? 바텀시트 내부에서 초기화 버튼을 누르면 어떻게 되나?
  // 1. 현재 선택중인 타입의 필터를 초기화 한다. -> api 재호출 됨
  //  - 하지만 이렇게 되면 날짜 선택의 경우 기획에서 start, end를 모두 null로 만드는데?
  //  - 그러니까 2번으로 가쟈.
  // 2. ui 만 초기화 한다. -> 적용 버튼을 눌러야 필터 초기화가 완료
  const handleReset = (type: FilterType) => {
    // 1.번사항 적용히 아래 주석 해제
    // setFilter({
    //   ...filter,
    //   [type]: initialFilter[type],
    // });
    setTempState({
      ...filter,
      [type]: initialFilter[type],
    });
  };

  const handleFilterChange = <T extends FilterType>(
    type: T,
    value: FilterValue<T>
  ) => {
    setTempState({
      ...filter,
      [type]: value,
    });
  };

  const handleApply = () => {
    setFilter(tempState);
    setIsOpen(false);
  };

  const handleDeleteKeyword = (
    label: string,
    type: keyof KeywordType,
    applyOn: 'filter' | 'temp'
  ) => {
    const updater = (prev: FilterState) => ({
      ...prev,
      keyword: {
        ...prev.keyword,
        [type]: prev.keyword[type].filter(item => item !== label),
      },
    });

    if (applyOn === 'filter') {
      setFilter(updater);
    }
    setTempState(updater);
  };

  useEffect(() => {
    if (openType === 'date') {
      if (tempState.date.start === null || tempState.date.end === null) {
        setIsApplyDisabled(true);
      } else {
        setIsApplyDisabled(false);
      }
    }
  }, [tempState, tempState.date]);

  return {
    filter,
    isOpen,
    openType,
    tempState,
    handleOpen,
    handleClose,
    handleReset,
    handleFilterChange,
    handleApply,
    handleDeleteKeyword,
    isApplyDisabled,
  };
}
