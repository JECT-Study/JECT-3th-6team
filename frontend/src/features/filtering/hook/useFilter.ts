'use client';

import { useEffect, useMemo, useRef, useState } from 'react';
import {
  FilterState,
  FilterType,
  FilterValue,
  KeywordType,
} from '@/features/filtering/hook/type';
import { usePathname, useRouter, useSearchParams } from 'next/navigation';
import {
  fromQuery,
  isEqualFilter,
  toQuery,
} from '@/features/filtering/lib/filterQuery';

export default function useFilter(initialFilter: FilterState) {
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();

  // 마운트 시 URL에 쿼리가 있었는지 기록
  const hasQueryOnMount = useRef<boolean | null>(null);
  if (hasQueryOnMount.current === null) {
    hasQueryOnMount.current = searchParams.toString().length > 0;
  }

  // 방금 쿼리 쓴 경우를 기억, 루프 방지
  const lastWrittenQueryRef = useRef<string>('');

  // 초기 상태 결정: URL 우선, 없으면 initialFilter
  const initialFromURL = useMemo(
    () => fromQuery(searchParams, initialFilter),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    []
  );
  const initialState = hasQueryOnMount.current ? initialFromURL : initialFilter;

  const [filter, setFilter] = useState<FilterState>(initialState); // 최종 필터 상태, 해당 값이 바뀌었을 시 api 호출
  const [tempState, setTempState] = useState<FilterState>(initialState); // 바텀 시트 내부 UI 상태 관리용, 적용 버튼을 눌러야 filter에 반영
  const [openType, setOpenType] = useState<FilterType | null>(null); // 바텀 시트 안의 내용물, 어떤 필터가 선택중인지.
  const [isOpen, setIsOpen] = useState(false); // 바텀 시트 open, close 조절
  const [isApplyDisabled, setIsApplyDisabled] = useState(false); // 적용 버튼의 disabled 상태 관리

  // URL ->  필터 상태반영 (초기/뒤로가기)
  useEffect(() => {
    const nowQ = searchParams.toString();
    if (lastWrittenQueryRef.current && lastWrittenQueryRef.current === nowQ)
      return;

    const urlFilter = fromQuery(searchParams, initialFilter);
    if (!isEqualFilter(filter, urlFilter)) {
      setFilter(urlFilter); // 여기서 데이터 패칭 트리거
      setTempState(urlFilter); // 바텀시트 동기화
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [searchParams]);

  // 필터 상태 -> URL 반영
  useEffect(() => {
    const qInURL = searchParams.toString();
    const q = toQuery(filter);

    // 초기 진입: 쿼리 없고 상태가 initial과 같으면 URL 건드리지 않음
    if (!hasQueryOnMount.current && isEqualFilter(filter, initialFilter))
      return;

    if (q === qInURL) return; // 동일하면 replace 불필요
    lastWrittenQueryRef.current = q;

    const url = q ? `${pathname}?${q}` : pathname;
    router.replace(url, { scroll: false });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filter, pathname, router]);

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
    setTempState(prev => ({
      ...prev,
      [type]: value,
    }));
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
  }, [tempState, tempState.date, openType]);

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
