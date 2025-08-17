'use client';

import { useState, useEffect, forwardRef } from 'react';
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
const KakaoMap = forwardRef<
  kakao.maps.Map,
  _MapProps &
    React.HTMLAttributes<HTMLDivElement> & {
      children?: ReactNode;
      isLoading?: boolean;
    }
>(({ center, level = 6, children, isLoading = false, ...props }, ref) => {
  const [isMapLoaded, setIsMapLoaded] = useState(false);
  const [mapError, setMapError] = useState<string | null>(null);
  const imageUrl = '/icons/Color/Icon_MyLocation.svg';

  useEffect(() => {
    // 카카오맵 SDK 로딩 확인
    const checkKakaoMapSDK = () => {
      if (typeof window !== 'undefined' && window.kakao && window.kakao.maps) {
        setIsMapLoaded(true);
        setMapError(null);
      } else {
        setMapError('지도를 불러올 수 없습니다.');
      }
    };

    // 초기 체크
    checkKakaoMapSDK();

    // SDK 로딩 대기
    const timer = setTimeout(checkKakaoMapSDK, 1000);

    return () => clearTimeout(timer);
  }, []);

  // 에러 상태 처리
  if (mapError) {
    return (
      <div className="w-full h-full bg-gray-100 flex items-center justify-center">
        <p className="text-gray-500 text-sm">{mapError}</p>
      </div>
    );
  }

  // 로딩 상태 처리
  if (!isMapLoaded || isLoading) {
    return (
      <div className="w-full h-full bg-gray-100 flex items-center justify-center">
        <p className="text-gray-500 text-sm">지도를 불러오는 중...</p>
      </div>
    );
  }

  return (
    <>
      <Map
        ref={ref}
        center={center}
        level={level}
        onError={error => {
          console.error('카카오맵 에러:', error);
          setMapError('지도를 표시할 수 없습니다.');
        }}
        {...props}
      >
        {/* <MapMarker
          position={center}
          image={{
            src: imageUrl,
            size: { width: 40, height: 40 },
          }}
        /> */}
        {children}
      </Map>
    </>
  );
});

KakaoMap.displayName = 'KakaoMap';

export default KakaoMap;
