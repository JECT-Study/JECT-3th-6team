'use client';

import { useState, useEffect } from 'react';

interface GeolocationState {
  latitude: number | null;
  longitude: number | null;
  error: string | null;
  isLoading: boolean;
}

export const useGeolocation = () => {
  const [state, setState] = useState<GeolocationState>({
    latitude: null,
    longitude: null,
    error: null,
    isLoading: true,
  });

  useEffect(() => {
    if (!navigator.geolocation) {
      setState(prev => ({
        ...prev,
        error: '이 브라우저에서는 위치 정보를 지원하지 않습니다.',
        isLoading: false,
      }));
      return;
    }

    const successHandler = (position: GeolocationPosition) => {
      setState({
        latitude: position.coords.latitude,
        longitude: position.coords.longitude,
        error: null,
        isLoading: false,
      });
    };

    const errorHandler = (error: GeolocationPositionError) => {
      // 권한 거부 시에만 에러로 처리
      if (error.code === error.PERMISSION_DENIED) {
        setState(prev => ({
          ...prev,
          error: '위치 정보 접근 권한이 거부되었습니다.',
          isLoading: false,
        }));
      } else {
        // 다른 에러들은 무시하고 로딩 상태 유지
        setState(prev => ({
          ...prev,
          isLoading: false,
        }));
      }
    };

    const options: PositionOptions = {
      enableHighAccuracy: true,
      timeout: 10000,
      maximumAge: 300000, // 5분
    };

    navigator.geolocation.getCurrentPosition(
      successHandler,
      errorHandler,
      options
    );
  }, []);

  return state;
};
