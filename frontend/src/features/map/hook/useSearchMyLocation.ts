import { useCallback } from 'react';

interface UseSearchMyLocationReturn {
  handleMoveToCurrentLocation: (
    mapRef: React.RefObject<kakao.maps.Map | null>
  ) => void;
}

export default function useSearchMyLocation(): UseSearchMyLocationReturn {
  const handleMoveToCurrentLocation = useCallback(
    (mapRef: React.RefObject<kakao.maps.Map | null>) => {
      if (mapRef.current) {
        const map = mapRef.current;
        if (map && map.panTo) {
          // 현재 위치 정보를 가져와서 지도 이동
          navigator.geolocation.getCurrentPosition(
            position => {
              const currentPos = new window.kakao.maps.LatLng(
                position.coords.latitude,
                position.coords.longitude
              );

              map.panTo(currentPos);
            },
            error => {
              if (error.code === error.PERMISSION_DENIED) {
                alert(
                  '위치 권한이 필요합니다. 브라우저 설정에서 위치 권한을 허용해주세요.'
                );
              } else {
                alert('현재 위치를 가져올 수 없습니다.');
              }
            },
            {
              enableHighAccuracy: true, // 높은 정확도
              timeout: 10000, // 10초 내로 위치 정보 얻지 못하면 타임아웃 에러 발생
              maximumAge: 60000, // 1분 이내 캐시된 위치 사용
            }
          );
        }
      }
    },
    []
  );

  return {
    handleMoveToCurrentLocation,
  };
}
