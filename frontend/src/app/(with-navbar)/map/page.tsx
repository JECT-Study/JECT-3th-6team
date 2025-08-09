'use client';

import { useEffect, useState } from 'react';

import { useGeolocation } from '@/shared/lib';

import { FilterProvider } from '@/features/filtering/lib/FilterContext';
import FilterBottomSheet from '@/features/filtering/ui/FilterBottomSheet';
import FilterGroupMapContainer from '@/features/map/ui/FilterGroupMapContainer';

export default function MapPage() {
  const { latitude, longitude, isLoading } = useGeolocation();

  // 기본 위치 (서울숲 4번출구 앞)
  const defaultCenter = { lat: 37.544643, lng: 127.044368 };
  const [center, setCenter] = useState(defaultCenter);

  // 위치 결정 로직:
  // 1. 로딩 중이면 지도 로딩 상태 유지 (지도를 불러오는 중... 표시)
  // 2. 권한 거부 시에만 기본 위치 사용
  // 3. 권한 허용 시 현재 위치 사용
  useEffect(() => {
    if (latitude && longitude) {
      setCenter({ lat: latitude, lng: longitude });
    }
  }, [latitude, longitude]);

  return (
    <FilterProvider>
      <FilterGroupMapContainer center={center} isLoading={isLoading} />
      <FilterBottomSheet />
    </FilterProvider>
  );
}
