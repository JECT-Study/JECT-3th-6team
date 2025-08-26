'use client';

import KeywordFilterPreview, { KeywordChip } from './KeywordFilterPreview';
import { useFilterContext } from '@/features/filtering/lib/FilterContext';
import toKeywordChips from '@/features/filtering/lib/makeKeywordChip';

export default function FilterKeywordSelect() {
  const { filter, handleOpen, handleDeleteKeyword } = useFilterContext();
  const { popupType, category } = filter.keyword;

  const keywords: KeywordChip[] = [
    ...toKeywordChips(popupType, 'popupType'),
    ...toKeywordChips(category, 'category'),
  ];

  return (
    <div className={'w-full bg-white rounded-t-[20px] pt-4.5 px-5 mt-[20px]'}>
      <KeywordFilterPreview
        initialStatus={'unselect'}
        onClick={() => handleOpen('keyword')}
        keywords={keywords}
        onDelete={({ label, type }) =>
          handleDeleteKeyword(label, type, 'filter')
        }
      />
    </div>
  );
}
