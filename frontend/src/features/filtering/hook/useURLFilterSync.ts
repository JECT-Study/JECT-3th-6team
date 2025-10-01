'use client';

import { usePathname, useRouter, useSearchParams } from 'next/navigation';
import { useEffect, useRef } from 'react';
import {
  fromQuery,
  isEqualFilter,
  toQuery,
} from '@/features/filtering/lib/filterQuery';
import { FilterState } from '@/features/filtering/hook/type';

type FilterSyncType = {
  filter: FilterState;
  initialFilter: FilterState;
  setFilter: (filter: FilterState) => void;
};

export default function useURLFilterSync({
  filter,
  initialFilter,
  setFilter,
}: FilterSyncType) {
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();

  // 마운트 시 URL에 쿼리가 있었는지 기록
  const hasQueryOnMount = useRef<boolean | null>(null);
  if (hasQueryOnMount.current === null) {
    hasQueryOnMount.current = searchParams.toString().length > 0;

    if (hasQueryOnMount.current) {
      const initialFromURL = fromQuery(searchParams, initialFilter);
      setFilter(initialFromURL); // URL 기반으로 초기화
    }
  }

  // 방금 쿼리 쓴 경우를 기억, 루프 방지
  const lastWrittenQueryRef = useRef<string>('');

  // URL ->  필터 상태반영 (초기/뒤로가기)
  useEffect(() => {
    const nowQ = searchParams.toString();
    if (lastWrittenQueryRef.current && lastWrittenQueryRef.current === nowQ)
      return;

    const urlFilter = fromQuery(searchParams, initialFilter);
    if (!isEqualFilter(filter, urlFilter)) {
      setFilter(urlFilter); // 여기서 데이터 패칭 트리거
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
}
