import KEYWORD_OPTIONS from '@/features/filtering/lib/keywordOptions';
import { ChipButton } from '@/shared/ui';

import { toast } from 'sonner';
import { KeywordType } from '@/features/filtering/hook/type';
import { KeywordChip } from '@/features/filtering/ui/KeywordFilterPreview';

const MAX_CATEGORY_LENGTH = 3;

export interface KeywordOptionProps {
  selected: KeywordType;
  onSelect: (value: KeywordType) => void;
  onDelete?: (chip: KeywordChip) => void;
}

export default function KeywordOption({
  selected,
  onSelect,
}: KeywordOptionProps) {
  const popupTypeOptions: string[] = KEYWORD_OPTIONS['popupType'];
  const categoryOptions: string[] = KEYWORD_OPTIONS['category'];
  const { category } = selected;

  const handleKeywordClick = (type: keyof KeywordType, value: string) => {
    const current = selected[type];

    // 이미 선택된 경우 → 제거
    if (current.includes(value)) {
      onSelect({
        ...selected,
        [type]: current.filter(item => item !== value),
      });
      return;
    }

    // 카테고리는 최대 3개 제한
    if (type === 'category' && current.length >= MAX_CATEGORY_LENGTH) {
      toast.info('카테고리는 최대 3개까지 선택가능해요');
      return;
    }

    // 추가
    onSelect({
      ...selected,
      [type]: [...current, value],
    });
  };

  return (
    <div className={'w-full mb-14 flex flex-col gap-y-[24px]'}>
      {/*팝업 타입 선택*/}
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
              onChipClick={() => handleKeywordClick('popupType', option)}
            />
          ))}
        </div>
      </div>

      {/*팝업 카테고리 선택*/}
      <div className={'w-full flex flex-col gap-y-[12px]'}>
        <div className={'flex flex-row items-center gap-[10px]'}>
          <h3 className={'text-[16px] text-black'}>카테고리</h3>
          <span className={'text-base text-gray60 font-regular'}>
            {category.length}/{MAX_CATEGORY_LENGTH}
          </span>
        </div>
        <div className={'w-full flex gap-x-2 gap-y-2 flex-wrap'}>
          {categoryOptions.map((option, index) => (
            <ChipButton
              color={'white'}
              key={index}
              label={option}
              disabled={false}
              isChecked={selected.category.includes(option)}
              onChipClick={() => handleKeywordClick('category', option)}
            />
          ))}
        </div>
      </div>
    </div>
  );
}
