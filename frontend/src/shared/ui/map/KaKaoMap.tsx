// 'use client';

import { Map, MapMarker } from 'react-kakao-maps-sdk';
import { _MapProps } from 'react-kakao-maps-sdk';

/**
 * 기본 카카오맵 컴포넌트
 *
 * center 값만 필수로 받고 MapMarker를 사용하는 기본적인 카카오맵입니다.
 * 지도의 중심점에 마커가 자동으로 표시됩니다.
 *
 * @param center - 지도의 중심 좌표 (필수)
 * @param props - react-kakao-maps-sdk의 _MapProps에서 지원하는 추가 props
 *
 * @example
 * ```tsx
 * import KakaoMap from '@/shared/ui/map/KaKaoMap';
 *
 * function MyComponent() {
 *   const center = { lat: 37.5665, lng: 126.9780 }; // 위도 경도값
 *
 *   return (
 *     <KakaoMap
 *       center={center}
 *     />
 *   );
 * }
 * ```
 */
export default function KakaoMap({ center, level = 6, ...props }: _MapProps) {
  return (
    <>
      <Map
        center={center}
        level={level}
        className="w-full h-48 rounded-[10px]"
        {...props}
      >
        <MapMarker position={center} />
      </Map>
    </>
  );
}
