'use client';

import { KakaoMap } from '@/shared/ui';
import { useGeolocation } from '@/shared/lib';

export default function MapPage() {
  const { latitude, longitude, error, isLoading } = useGeolocation();

  // 기본 위치 (서울 시청)
  const defaultCenter = { lat: 37.5665, lng: 126.978 };

  // 현재 위치가 있으면 사용, 없으면 기본 위치 사용
  const center =
    latitude && longitude ? { lat: latitude, lng: longitude } : defaultCenter;

  if (isLoading) {
    return (
      <div className="w-full h-screen pb-[120px] flex items-center justify-center">
        <p className="text-gray-500 text-sm">현재 위치를 가져오는 중...</p>
      </div>
    );
  }

  return (
    <div className="w-full h-screen pb-[120px]">
      {/* {error && (
        <div className="absolute top-4 left-4 right-4 z-10 bg-yellow-50 border border-yellow-200 rounded-lg p-3 shadow-sm">
          <p className="text-yellow-800 text-sm">{error}</p>
          <p className="text-yellow-600 text-xs mt-1">
            기본 위치로 지도가 표시됩니다.
          </p>
        </div>
      )} */}
      <KakaoMap center={center} level={3} className="w-full h-full" />
    </div>
  );
}
