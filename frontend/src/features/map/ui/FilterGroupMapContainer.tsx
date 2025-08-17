'use client';

import { useRef, useState, useCallback } from 'react';
import { MapMarker } from 'react-kakao-maps-sdk';
import { useQuery } from '@tanstack/react-query';

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

import { getMapPopupListApi } from '@/entities/map/api';
import getPopupListApi from '@/entities/popup/api/getPopupListApi';
import BadgedPopupCard from '@/entities/popup/ui/BadgedPopupCard';
import { PopupItemType } from '@/entities/popup/types/PopupListItem';

export default function FilterGroupMapContainer() {
  // 기본 위치 (서울숲 4번출구 앞)
  const defaultCenter = { lat: 37.544643, lng: 127.044368 };
  const [center, setCenter] = useState(defaultCenter);
  const [selectedPopupId, setSelectedPopupId] = useState<number | null>(null);
  const [selectedPopupData, setSelectedPopupData] =
    useState<PopupItemType | null>(null);
  const [searchKeyword, setSearchKeyword] = useState('');

  const { filter, handleOpen, handleDeleteKeyword } = useFilterContext();
  const { popupType, category } = filter.keyword;
  const mapRef = useRef<kakao.maps.Map>(null);
  const { handleMoveToCurrentLocation } = useSearchMyLocation();
  const {
    searchValue,
    isSearchFocused,
    handleSearchFocus,
    handleSearchBlur,
    handleChange,
  } = useMapSearch();

  const { data: searchResponse } = useQuery({
    queryKey: ['popup', 'list', { keyword: searchKeyword }],
    queryFn: async () => {
      const result = await getPopupListApi({ keyword: searchKeyword });
      console.log('검색 결과:', result);
      return result;
    },
    enabled: !!searchKeyword,
  });

  const handleKeyDown = useCallback(
    (event: React.KeyboardEvent<HTMLInputElement>) => {
      if (event.key === 'Enter' && searchValue.trim()) {
        setSearchKeyword(searchValue.trim());
      }
    },
    [searchValue]
  );
  const popupListIconSrc = '/icons/Color/Icon_NormalMinus.svg';
  const selectedPopupIconSrc = '/icons/Color/Icon_Map.svg';

  const keywords: KeywordChip[] = [
    ...toKeywordChips(popupType, 'category'),
    ...toKeywordChips(category, 'category'),
  ];

  const handleMarkerClick = async (popupId: number) => {
    const isCurrentlySelected = selectedPopupId === popupId;

    if (isCurrentlySelected) {
      return;
    }

    // 새로운 마커 선택
    setSelectedPopupId(popupId);
    console.log('선택된 팝업 ID:', popupId);

    try {
      console.log('🔄 팝업 상세 데이터 요청 시도...');
      const popupData = await getPopupListApi({ popupId });
      console.log('✅ 팝업 데이터 성공:', popupData);

      // API 응답에서 첫 번째 팝업 데이터를 selectedPopupData로 설정
      if (popupData.content && popupData.content.length > 0) {
        setSelectedPopupData(popupData.content[0]);
      }
    } catch (error) {
      console.error('❌ 팝업 데이터 조회 실패, 목 데이터 사용:', error);

      // 목 데이터 생성
      const mockPopupData = {
        popupId: popupId,
        popupName: `팝업 스토어 ${popupId}`,
        location: {
          addressName: '서울특별시 강남구 청담동',
          region1depthName: '서울특별시',
          region2depthName: '강남구',
          region3depthName: '청담동',
          latitude: 37.543401,
          longitude: 127.04452,
        },
        rating: {
          averageStar: 4.5,
          reviewCount: 123,
        },
        period: '2025.01.01 ~ 2025.12.31',
        dDay: 365,
        popupImageUrl: '/images/popup-ex.png',
        searchTags: {
          type: '체험형',
          category: ['패션'],
        },
        tag: 'DEFAULT' as const,
      };

      setSelectedPopupData(mockPopupData);
      console.log('📤 목 팝업 데이터 사용:', mockPopupData);
    }
  };

  // 위치 결정 로직:
  // 1. 로딩 중이면 지도 로딩 상태 유지 (지도를 불러오는 중... 표시)
  // 2. 로딩 완료 후 기본 위치(서울숲역 4번출구) 사용
  // 3. 내위치찾기 버튼 클릭 시 현재 위치 추적 - 이때 권한설정 팝업

  // 임시 목 데이터 (MSW 대신 사용)
  const mockPopupList = {
    popupList: [
      {
        id: 1,
        latitude: 37.544,
        longitude: 127.0436,
      },
      {
        id: 2,
        latitude: 37.545470791421,
        longitude: 127.04324359055,
      },
      {
        id: 3,
        latitude: 37.545470791421,
        longitude: 127.04324359055,
      },
    ],
  };

  const { data: popupList } = useQuery({
    queryKey: ['mapPopupList', popupType, category],
    queryFn: async () => {
      console.log('🔄 API 요청 시도...');
      try {
        const result = await getMapPopupListApi({
          minLatitude: 37.541673,
          maxLatitude: 37.545894,
          minLongitude: 127.041309,
          maxLongitude: 127.047804,
          type: popupType.length > 0 ? popupType.join(',') : undefined,
          category: category.length > 0 ? category.join(',') : undefined,
        });
        console.log('✅ API 성공:', result);
        return result;
      } catch (error) {
        console.error('❌ API 실패, 목 데이터 사용:', error);
        return mockPopupList; // API 실패 시 목 데이터 반환
      }
    },
  });

  return (
    <div className="w-full h-screen pb-[100px] relative">
      {/* 검색 포커스 시 지도 영역만 오버레이 */}
      {isSearchFocused && (
        <div className="absolute top-0 left-0 right-0 bottom-[100px] z-30 bg-white">
          {/* 검색 입력창과 결과를 포함하는 컨테이너 */}
          <div className="absolute top-4 left-1/2 transform -translate-x-1/2 z-30 w-[400px]">
            {/* 검색 입력창 */}
            <div className="rounded-2xl bg-white shadow-[0_2px_10px_0_rgba(0,0,0,0.05)] backdrop-blur-[5px] p-3 mb-3">
              <SearchInput
                id={'search-input'}
                value={searchValue}
                onChange={handleChange}
                onFocus={handleSearchFocus}
                onBlur={handleSearchBlur}
                onKeyDown={handleKeyDown}
              />
            </div>

            {/* 검색 결과 표시 */}
            {searchResponse?.content && searchResponse.content.length > 0 && (
              <div className="max-h-[calc(100vh-200px)] overflow-y-auto space-y-2  bg-white  p-3">
                {searchResponse.content.map(popup => {
                  return (
                    <div key={popup.popupId}>
                      <BadgedPopupCard {...popup} />
                    </div>
                  );
                })}
              </div>
            )}

            {/* 검색 결과가 없을 때 */}
            {searchKeyword &&
              searchResponse?.content &&
              searchResponse.content.length === 0 && (
                <div className="p-4 text-center text-gray-500 rounded-2xl bg-white shadow-[0_2px_10px_0_rgba(0,0,0,0.05)]">
                  검색 결과가 없습니다.
                </div>
              )}
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
              onKeyDown={handleKeyDown}
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
          >
            {popupList?.popupList?.map(popup => (
              <MapMarker
                key={popup.id}
                position={{ lat: popup.latitude, lng: popup.longitude }}
                image={{
                  src:
                    selectedPopupId === popup.id
                      ? selectedPopupIconSrc
                      : popupListIconSrc,
                  size: { width: 32, height: 32 },
                }}
                onClick={() => handleMarkerClick(popup.id)}
              />
            ))}
          </KakaoMap>
        </>
      )}

      <MyLocationButton
        onMoveToCurrentLocation={() =>
          handleMoveToCurrentLocation(mapRef, setCenter)
        }
      />
      {selectedPopupData && (
        <div className="absolute bottom-30 left-0 right-0 z-50">
          <div className="w-full max-w-sm mx-auto transform scale-100">
            <BadgedPopupCard {...selectedPopupData} />
          </div>
        </div>
      )}
    </div>
  );
}
