import { MapPopupListRequestDto } from '@/entities/map/types/type';

/**
 * React Ref를 통해 카카오맵의 bounds 정보를 추출하는 유틸 함수
 *
 * @param mapRef - useRef로 생성된 카카오맵 ref 객체
 * @returns MapPopupListRequestDto에서 필요한 4개 좌표 값
 *
 * @example
 * ```typescript
 * import { getMapBounds } from '@/shared/lib';
 *
 * const mapRef = useRef<kakao.maps.Map>(null);
 *
 * const handleGetBounds = () => {
 *   const bounds = getMapBounds(mapRef);
 *   if (bounds) {
 *     getMapPopupListApi(bounds);
 *   }
 * };
 * ```
 */
export const useGetMapBounds = (
  mapRef: React.RefObject<kakao.maps.Map>
): Pick<
  MapPopupListRequestDto,
  'minLatitude' | 'maxLatitude' | 'minLongitude' | 'maxLongitude'
> | null => {
  const map = mapRef.current;

  if (!map) {
    console.warn('getMapBounds: map 인스턴스가 null입니다.');
    return null;
  }

  try {
    // 현재 지도 영역의 영역정보를 얻어옵니다
    const bounds = map.getBounds();

    // 영역의 남서쪽 좌표 (최소 위도, 최소 경도)
    const swLatLng = bounds.getSouthWest();
    const minLatitude = swLatLng.getLat();
    const minLongitude = swLatLng.getLng();

    // 영역의 북동쪽 좌표 (최대 위도, 최대 경도)
    const neLatLng = bounds.getNorthEast();
    const maxLatitude = neLatLng.getLat();
    const maxLongitude = neLatLng.getLng();

    return {
      minLatitude,
      maxLatitude,
      minLongitude,
      maxLongitude,
    };
  } catch (error) {
    console.error('getMapBounds: bounds 정보를 가져오는 중 오류 발생:', error);
    return null;
  }
};
