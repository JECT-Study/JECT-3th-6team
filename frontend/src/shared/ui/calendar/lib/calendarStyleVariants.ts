// calendarCellVariants.ts
import { tv } from 'tailwind-variants';

export const dateCell = tv({
  // 항상 들어가는 공통 클래스
  base: 'relative font-regular text-[20px] z-20 w-[44px] h-[44px] flex justify-center items-center',
  // 상태별 조건 분기 클래스
  variants: {
    isToday: {
      true: 'text-main font-semibold',
    },
    isSelected: {
      true: 'bg-main text-white rounded-full font-semibold',
      false: '',
    },
    isStartDate: {
      true: 'bg-main text-white rounded-full font-semibold',
      false: '',
    },
    isEndDate: {
      true: 'bg-main text-white rounded-full font-semibold',
    },
    isFromToday: {
      true: 'bg-main-pale text-main',
    },
    isCurrentMonth: {
      false: 'text-gray60',
    },
  },
  compoundVariants: [
    {
      isToday: true,
      isSelected: true,
      class: 'bg-main-pale text-main',
    },
    {
      isToday: true,
      isStartDate: true,
      class: 'bg-main-pale text-main',
    },
    {
      isToday: true,
      isEndDate: true,
      class: 'bg-main-pale text-main',
    },
  ],
});

export const rangeBlock = tv({
  base: 'absolute -inset-x-2',
  variants: {
    isInRange: {
      true: 'inset-y-2 block bg-sub2 z-0',
    },
    isStartDate: {
      true: 'left-1/2 -right-1 inset-y-2 block bg-sub2 z-0',
    },
    isEndDate: {
      true: 'right-1/2 -left-1 inset-y-2 block bg-sub2 z-0',
    },
  },
});
