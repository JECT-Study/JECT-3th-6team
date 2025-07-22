'use client';

import { Map, MapMarker } from 'react-kakao-maps-sdk';
import { _MapProps } from 'react-kakao-maps-sdk';
import { ReactNode } from 'react';

/**
 * 기본 카카오맵 컴포넌트
 *
 * center 값만 필수로 받고 MapMarker를 사용하는 기본적인 카카오맵입니다.
 * children으로 여러 마커를 전달할 수 있으며, children이 없으면 중심점에 자동으로 마커가 표시됩니다.
 *
 * @param center - 지도의 중심 좌표 (필수)
 * @param children - MapMarker 컴포넌트들을 포함한 children (선택)
 * @param props - react-kakao-maps-sdk의 _MapProps와 HTML 속성들을 포함한 모든 props
 *
 * @example
 * ```tsx
 * import KakaoMap from '@/shared/ui/map/KaKaoMap';
 * import { MapMarker } from 'react-kakao-maps-sdk';
 *
 * function MyComponent() {
 *   const center = { lat: 37.5665, lng: 126.9780 };
 *   const positions = [
 *     { id: 1, lat: 37.5665, lng: 126.9780 },
 *     { id: 2, lat: 37.5666, lng: 126.9781 },
 *   ];
 *
 *   return (
 *     <KakaoMap
 *       center={center}
 *       className="w-full h-full"
 *     >
 *       {positions.map(position => (
 *         <MapMarker
 *           key={position.id}
 *           position={{ lat: position.lat, lng: position.lng }}
 *         />
 *       ))}
 *     </KakaoMap>
 *   );
 * }
 * ```
 */
export default function KakaoMap({
  center,
  level = 6,
  children,
  ...props
}: _MapProps &
  React.HTMLAttributes<HTMLDivElement> & { children?: ReactNode }) {
  const imageUrl = '/icons/Color/Icon_map.svg';
  return (
    <>
      <Map center={center} level={level} {...props}>
        {children || (
          <MapMarker
            position={center}
            image={{
              src: imageUrl,
              size: { width: 40, height: 40 },
            }}
          />
        )}
      </Map>
    </>
  );
}
