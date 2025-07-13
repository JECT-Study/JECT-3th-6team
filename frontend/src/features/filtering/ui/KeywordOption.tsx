import KEYWORD_OPTIONS from '@/features/filtering/lib/keywordOptions';
import { ChipButton } from '@/shared/ui';
import KeywordFilterPreview, { KeywordChip } from './KeywordFilterPreview';
import { KeywordType } from '@/features/filtering/hook/useFilter';
import { toast } from 'sonner';
import toKeywordChips from '@/features/filtering/lib/makeKeywordChip';

interface KeywordOptionProps {
  selected: KeywordType;
  onSelect: (value: KeywordType) => void;
  onDelete: (chip: KeywordChip) => void;
}

export default function KeywordOption({
  selected,
  onSelect,
  onDelete,
}: KeywordOptionProps) {
  const popupTypeOptions: string[] = KEYWORD_OPTIONS['popupType'];
  const categoryOptions: string[] = KEYWORD_OPTIONS['category'];
  const { popupType, category } = selected;

  const keywords: KeywordChip[] = [
    ...toKeywordChips(popupType, 'popupType'),
    ...toKeywordChips(category, 'category'),
  ];

  // TODO : 하나의 핸들러로 합치기
  const handleCategoryClick = (category: string) => {
    // 삭제
    if (selected.category.includes(category)) {
      const next = {
        ...selected,
        category: selected.category.filter(type => type !== category),
      };
      onSelect(next);
      return;
    }

    // 추가
    if (selected.category.length >= 3) {
      toast.info('카테고리는 최대 3개까지 선택가능해요');
      return;
    }

    const next = {
      ...selected,
      category: [...selected.category, category],
    };
    onSelect(next);
  };

  const handlePopupTypeClick = (popupType: string) => {
    // 삭제
    if (selected.popupType.includes(popupType)) {
      const next = {
        ...selected,
        popupType: selected.popupType.filter(type => type !== popupType),
      };
      onSelect(next);
      return;
    }

    const next = {
      ...selected,
      popupType: [...selected.popupType, popupType],
    };
    onSelect(next);
  };
  return (
    <div className={'w-full mb-14 flex flex-col gap-y-[24px]'}>
      <KeywordFilterPreview
        initialStatus={'select'}
        onClick={() => {
          alert('click');
        }}
        keywords={keywords}
        onDelete={onDelete}
      />
      {/* 최대 3개 선택 */}
      <div className={'flex flex-col gap-y-[12px]'}>
        <h3 className={'text-[16px] text-black'}>팝업 유형</h3>
        <div className={'flex gap-x-2 items-center flex-wrap'}>
          {popupTypeOptions.map((option, index) => (
            <ChipButton
              key={index}
              color={'white'}
              label={option}
              disabled={false}
              isChecked={selected.popupType.includes(option)}
              onChipClick={() => handlePopupTypeClick(option)}
            />
          ))}
        </div>
      </div>

      {/*최대 3개 선택*/}
      <div className={'w-full flex flex-col gap-y-[12px]'}>
        <h3 className={'text-[16px] text-black'}>카테고리</h3>
        <div className={'w-full flex gap-x-2 gap-y-2 flex-wrap'}>
          {categoryOptions.map((option, index) => (
            <ChipButton
              color={'white'}
              key={index}
              label={option}
              disabled={false}
              isChecked={selected.category.includes(option)}
              onChipClick={() => handleCategoryClick(option)}
            />
          ))}
        </div>
      </div>
    </div>
  );
}
