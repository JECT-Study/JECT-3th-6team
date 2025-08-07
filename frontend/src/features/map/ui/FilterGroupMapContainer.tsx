'use client';

import { useRef } from 'react';
import { KakaoMap } from '@/shared/ui';
import SearchInput from '@/shared/ui/input/SearchInput';
import MyLocationButton from '@/shared/ui/map/MyLocationButton';

import { useFilterContext } from '@/features/filtering/lib/FilterContext';
import KeywordFilterPreview, {
  KeywordChip,
} from '@/features/filtering/ui/KeywordFilterPreview';
import toKeywordChips from '@/features/filtering/lib/makeKeywordChip';
import useSearchMyLocation from '@/features/map/hook/useSearchMyLocation';
import useMapSearch from '@/features/map/hook/useMapSearch';

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
  const mapRef = useRef<any>(null);
  const { handleMoveToCurrentLocation } = useSearchMyLocation();
  const {
    searchValue,
    isSearchFocused,
    handleSearchFocus,
    handleSearchBlur,
    handleChange,
  } = useMapSearch();

  const keywords: KeywordChip[] = [
    ...toKeywordChips(popupType, 'category'),
    ...toKeywordChips(category, 'category'),
  ];

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

      <MyLocationButton
        onMoveToCurrentLocation={() => handleMoveToCurrentLocation(mapRef)}
      />
    </div>
  );
}
