'use client';

import { useState, useEffect } from 'react';
import { Map, Circle } from 'react-kakao-maps-sdk';
import type { CircleProps, _MapProps } from 'react-kakao-maps-sdk';

interface CircleMapProps extends _MapProps {
  circleOptions?: CircleProps;
}

export default function CircleMap({
  center,
  level = 6,
  minLevel,
  maxLevel,
  onClick,
  circleOptions = {
    center: center as { lat: number; lng: number },
    radius: 200,
    strokeWeight: 4,
    strokeColor: '#75B8FA',
    strokeOpacity: 2,
    strokeStyle: 'solid',
    fillColor: '#CFE7FF',
    fillOpacity: 0.7,
  },
  ...props
}: CircleMapProps) {
  const [isMapLoaded, setIsMapLoaded] = useState(false);
  const [mapError, setMapError] = useState<string | null>(null);

  useEffect(() => {
    // 카카오맵 SDK 로딩 확인
    const checkKakaoMapSDK = () => {
      if (typeof window !== 'undefined' && window.kakao && window.kakao.maps) {
        setIsMapLoaded(true);
        setMapError(null);
      } else {
        setMapError('카카오맵을 불러올 수 없습니다.');
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
      <div className="w-full h-30 rounded-[10px] bg-gray-100 flex items-center justify-center">
        <p className="text-gray-500 text-sm">{mapError}</p>
      </div>
    );
  }

  // 로딩 상태 처리
  if (!isMapLoaded) {
    return (
      <div className="w-full h-30 rounded-[10px] bg-gray-100 flex items-center justify-center">
        <p className="text-gray-500 text-sm">지도를 불러오는 중...</p>
      </div>
    );
  }

  return (
    <Map
      center={center}
      level={level}
      maxLevel={maxLevel}
      minLevel={minLevel}
      draggable={false}
      onClick={onClick}
      className="w-full h-30 rounded-[10px]"
      onError={error => {
        console.error('카카오맵 에러:', error);
        setMapError('지도를 표시할 수 없습니다.');
      }}
      {...props}
    >
      <Circle {...circleOptions} />
    </Map>
  );
}
