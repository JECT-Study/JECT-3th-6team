'use client';

import { useEffect, useState } from 'react';

import { useGeolocation } from '@/shared/lib';

import { FilterProvider } from '@/features/filtering/lib/FilterContext';
import FilterBottomSheet from '@/features/filtering/ui/FilterBottomSheet';
import FilterGroupMapContainer from './FilterGroupMapContainer';

export default function MapPage() {
  const { latitude, longitude, error, isLoading } = useGeolocation();
  const [mounted, setMounted] = useState(false);

  // 기본 위치 (서울 시청)
  const defaultCenter = { lat: 37.5665, lng: 126.978 };

  // 클라이언트에서만 현재 위치 사용
  const center =
    mounted && latitude && longitude
      ? { lat: latitude, lng: longitude }
      : defaultCenter;

  useEffect(() => {
    setMounted(true);
  }, []);

  return (
    <FilterProvider>
      <FilterGroupMapContainer center={center} />
      <FilterBottomSheet />
    </FilterProvider>
  );
}
