'use client';

import IconFilterMain from '@/assets/icons/Normal/Icon_Filter_main.svg';
import IconFilterWhite from '@/assets/icons/Normal/Icon_Filter_White.svg';
import { tv } from 'tailwind-variants';
import React, { useEffect, useState } from 'react';
import { Chip } from '@/shared/ui/chip/Chip';
import { KeywordType } from '@/features/filtering/hook/useFilter';

/**
 *  2가지 상태
 *  1. unselect  (아무것도 선택되지 않음) : 노랑색 아이콘 + Placeholder
 *  2. select (선택된 키워드가 있음) : 주황색 아이콘 + 선택된 필터들이 위치
 */
type statusType = 'unselect' | 'select';
export type KeywordChip = {
  label: string; // 예: "디저트", "브랜드 팝업"
  type: keyof KeywordType; // 'popupType' | 'category'
};

interface KeywordFilterPreviewProps {
  initialStatus: statusType;
  onClick: () => void;
  keywords?: KeywordChip[];
  onDelete?: (chip: KeywordChip) => void;
}

const filterIcons = tv({
  base: 'absolute rounded-full w-[40px] h-[40px] flex justify-center items-center z-10',
  variants: {
    status: {
      unselect: 'bg-sub2 border border-main',
      select: 'bg-main',
    },
  },
});

export default function KeywordFilterPreview({
  initialStatus,
  onClick,
  keywords,
  onDelete,
}: KeywordFilterPreviewProps) {
  const [status, setStatus] = useState<statusType>(initialStatus);

  useEffect(() => {
    // 예외 케이스 : 선택사항이 없어도 select 상태의 ui 표시
    if (initialStatus === 'select' && keywords && keywords.length === 0) {
      setStatus('select');
      return;
    }

    // 일반 : 선택된 키워드 유무에 따라 분기
    if (keywords && keywords.length > 0) {
      setStatus('select');
    } else {
      setStatus('unselect');
    }
  }, [keywords]);
  return (
    // wrapper
    <div className={'relative w-full h-[40px]'}>
      <div className={filterIcons({ status })} onClick={onClick}>
        {status === 'unselect' ? (
          <IconFilterMain width={20} height={20} />
        ) : (
          <IconFilterWhite width={20} height={20} />
        )}
      </div>
      <div
        className={
          'absolute top-0 left-0 w-full h-[40px] border border-sub rounded-full z-0 pl-[50px] pr-[15px] flex items-center'
        }
      >
        {status === 'unselect' && (
          <p
            className={
              'w-full min-w-[200px] text-[14px] text-gray60 font-light  '
            }
          >
            원하는 조건을 선택해 보세요!
          </p>
        )}

        <div
          className={'w-full flex items-center gap-x-[10px]  overflow-x-scroll'}
          style={{
            scrollbarWidth: 'none', // Firefox
            msOverflowStyle: 'none', // IE
          }}
        >
          {status === 'select' &&
            keywords &&
            keywords.length > 0 &&
            keywords.map((chip, index) => {
              return (
                <Chip
                  iconPosition={'right'}
                  key={index}
                  className={'py-[7px] text-main'}
                  onClickRightIcon={() => onDelete?.(chip)}
                >
                  {chip.label}
                </Chip>
              );
            })}
        </div>
      </div>
    </div>
  );
}
