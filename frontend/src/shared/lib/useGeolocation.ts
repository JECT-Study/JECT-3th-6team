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
      let errorMessage = '위치 정보를 가져올 수 없습니다.';

      switch (error.code) {
        case error.PERMISSION_DENIED:
          errorMessage = '위치 정보 접근 권한이 거부되었습니다.';
          break;
        case error.POSITION_UNAVAILABLE:
          errorMessage = '위치 정보를 사용할 수 없습니다.';
          break;
        case error.TIMEOUT:
          errorMessage = '위치 정보 요청 시간이 초과되었습니다.';
          break;
      }

      setState(prev => ({
        ...prev,
        error: errorMessage,
        isLoading: false,
      }));
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
