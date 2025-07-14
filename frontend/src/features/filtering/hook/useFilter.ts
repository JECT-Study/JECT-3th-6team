'use client';

import { useEffect, useState } from 'react';
import {
  FilterState,
  FilterType,
  FilterValue,
} from '@/features/filtering/hook/type';

export default function useFilter(initialFilter: FilterState) {
  const [filter, setFilter] = useState<FilterState>(initialFilter); // 최종 필터 상태, 해당 값이 바뀌었을 시 api 호출
  const [tempState, setTempState] = useState<FilterState>(initialFilter); // 바텀 시트 내부 UI 상태 관리용, 적용 버튼을 눌러야 filter에 반영
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

  const handleReset = (type: FilterType) => {
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
