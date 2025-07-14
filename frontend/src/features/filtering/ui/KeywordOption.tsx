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
    if (type === 'category' && current.length >= 3) {
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
      <KeywordFilterPreview
        initialStatus={'select'}
        onClick={() => {
          alert('click');
        }}
        keywords={keywords}
        onDelete={onDelete}
      />

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
        <h3 className={'text-[16px] text-black'}>카테고리</h3>
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
