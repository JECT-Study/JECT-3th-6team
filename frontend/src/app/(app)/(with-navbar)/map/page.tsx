'use client';

import { FilterProvider } from '@/features/filtering/lib/FilterContext';
import FilterBottomSheet from '@/features/filtering/ui/FilterBottomSheet';
import FilterGroupMapContainer from '@/features/map/ui/FilterGroupMapContainer';
import { Suspense } from 'react';

export default function MapPage() {
  return (
    // NOTE : 내부에 useSearchParams를 사용하는 훅이 있을 경우, 상위를 Suspense로 감싸야 합니다.
    // TODO :  build에러 막기위해 임시로 Suspense 추가, 이후 Fallback UI를 수정하십시오 @hyunjee
    <Suspense fallback={<div>로딩중</div>}>
      <FilterProvider>
        <FilterGroupMapContainer />
        <FilterBottomSheet />
      </FilterProvider>
    </Suspense>
  );
}
