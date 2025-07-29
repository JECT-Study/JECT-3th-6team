'use client';

import { KakaoMap } from '@/shared/ui';
import FilterKeywordSelect from '@/features/filtering/ui/FilterKeywordSelect';
import { useFilterContext } from '@/features/filtering/lib/FilterContext';
import KeywordFilterPreview, {
  KeywordChip,
} from '@/features/filtering/ui/KeywordFilterPreview';
import toKeywordChips from '@/features/filtering/lib/makeKeywordChip';

interface MapContentProps {
  center: { lat: number; lng: number };
}

export default function MapContent({ center }: MapContentProps) {
  const { filter, handleOpen, handleDeleteKeyword } = useFilterContext();
  const { popupType, category } = filter.keyword;

  const keywords: KeywordChip[] = [
    ...toKeywordChips(popupType, 'popupType'),
    ...toKeywordChips(category, 'category'),
  ];

  return (
    <div className="w-full h-screen pb-[120px]">
      <div className="absolute top-4 left-1/2 transform -translate-x-1/2 z-30 w-[359px] h-[122px] rounded-2xl bg-white/60 shadow-[0_2px_10px_0_rgba(0,0,0,0.05)] backdrop-blur-[5px] p-4">
        <KeywordFilterPreview
          initialStatus="unselect"
          onClick={() => handleOpen('keyword')}
          keywords={keywords}
          onDelete={({ label, type }) =>
            handleDeleteKeyword(label, type, 'filter')
          }
        />
      </div>
      {/* 검색 및 필터 UI */}
      {/* <MapSearchFilter
        onSearchClick={handleSearchClick}
        onFilterClick={handleFilterClick}
      /> */}

      <KakaoMap center={center} level={3} className="w-full h-full" />
    </div>
  );
}
