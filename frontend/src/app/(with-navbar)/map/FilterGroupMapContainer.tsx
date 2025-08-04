'use client';

import { useState, useRef } from 'react';
import { KakaoMap } from '@/shared/ui';
import SearchInput from '@/shared/ui/input/SearchInput';
import MyLocationButton from '@/shared/ui/map/MyLocationButton';

import { useFilterContext } from '@/features/filtering/lib/FilterContext';
import KeywordFilterPreview, {
  KeywordChip,
} from '@/features/filtering/ui/KeywordFilterPreview';
import toKeywordChips from '@/features/filtering/lib/makeKeywordChip';

interface MapContentProps {
  center: { lat: number; lng: number };
  isLoading?: boolean;
}

export default function FilterGroupMapContainer({
  center,
  isLoading = false,
}: MapContentProps) {
  const { filter, handleOpen, handleDeleteKeyword } = useFilterContext();
  const { popupType, category } = filter.keyword;
  const [isSearchFocused, setIsSearchFocused] = useState(false);
  const [searchValue, setSearchValue] = useState('');
  const mapRef = useRef<any>(null);

  const keywords: KeywordChip[] = [
    ...toKeywordChips(popupType, 'category'),
    ...toKeywordChips(category, 'category'),
  ];

  const handleSearchFocus = () => {
    setIsSearchFocused(true);
  };

  const handleSearchBlur = () => {
    setIsSearchFocused(false);
  };

  const handleChange = (value: string) => {
    setSearchValue(value);
    // TODO : 검색 로직 구현
    console.log('search', value);
  };

  const handleMoveToCurrentLocation = () => {
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
            // const currentPos = {
            //   lat: position.coords.latitude,
            //   lng: position.coords.longitude,
            // };
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
  };

  return (
    <div className="w-full h-screen pb-[120px] relative">
      {/* 검색 포커스 시 지도 영역만 오버레이 */}
      {isSearchFocused && (
        <div className="absolute top-0 left-0 right-0 bottom-[120px] z-30 bg-white">
          <div className="absolute top-4 left-1/2 transform -translate-x-1/2 z-30 w-[400px] h-75px] rounded-2xl bg-white shadow-[0_2px_10px_0_rgba(0,0,0,0.05)] backdrop-blur-[5px] p-3">
            <SearchInput
              id={'search-input'}
              value={searchValue}
              onChange={handleChange}
              onFocus={handleSearchFocus}
              onBlur={handleSearchBlur}
            />
          </div>
        </div>
      )}

      {/* 일반 상태 */}
      {!isSearchFocused && (
        <>
          <div className="absolute top-4 left-1/2 transform -translate-x-1/2 z-30 w-[400px] h-[122px] rounded-2xl bg-white/60 shadow-[0_2px_10px_0_rgba(0,0,0,0.05)] backdrop-blur-[5px] p-3 space-y-2.5">
            <SearchInput
              id={'search-input'}
              value={searchValue}
              onChange={handleChange}
              onFocus={handleSearchFocus}
              onBlur={handleSearchBlur}
            />

            <KeywordFilterPreview
              initialStatus="unselect"
              onClick={() => handleOpen('keyword')}
              keywords={keywords}
              onDelete={({ label, type }) =>
                handleDeleteKeyword(label, type, 'filter')
              }
            />
          </div>

          <KakaoMap
            ref={mapRef}
            center={center}
            level={3}
            className="w-full h-full"
            isLoading={isLoading}
          />
        </>
      )}

      <MyLocationButton onMoveToCurrentLocation={handleMoveToCurrentLocation} />
    </div>
  );
}
