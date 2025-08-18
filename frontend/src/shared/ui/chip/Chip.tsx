import * as React from 'react';
import { Slot } from '@radix-ui/react-slot';
import { cva, type VariantProps } from 'class-variance-authority';
import Image from 'next/image';

import { cn } from '@/lib/utils';

const chipVariants = cva(
  'inline-flex justify-center rounded-full px-3 py-2 text-xs font-medium w-fit whitespace-nowrap shrink-0 gap-2.5 focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden',
  {
    variants: {
      variant: {
        white: 'bg-white text-gray80 border border-gray40',
        main: 'bg-main text-white',
        mainLight: 'bg-white text-main border border-gray40',
      },
      iconPosition: {
        left: 'flex-row',
        right: 'flex-row-reverse',
        none: '',
      },
    },
    defaultVariants: {
      variant: 'white',
      iconPosition: 'none',
    },
  }
);

export interface ChipProps
  extends React.ComponentProps<'span'>,
    VariantProps<typeof chipVariants> {
  asChild?: boolean;
  icon?: React.ReactNode;
  iconPosition?: 'left' | 'right' | 'none';
  onClickRightIcon?: () => void;
}

function Chip({
  className,
  variant,
  iconPosition = 'none',
  icon,
  children,
  asChild = false,
  onClickRightIcon,
  ...props
}: ChipProps) {
  const Comp = asChild ? Slot : 'span';

  // iconPosition이 "right"이고 icon이 제공되지 않았을 때 기본 X 아이콘 사용
  const defaultIcon =
    iconPosition === 'right' && !icon ? (
      <Image
        src="/icons/Normal/Icon_X.svg"
        alt="Close"
        width={12}
        height={12}
        className="size-3 cursor-pointer"
        onClick={onClickRightIcon}
      />
    ) : (
      icon
    );

  return (
    <Comp
      data-slot="chip"
      className={cn(chipVariants({ variant, iconPosition }), className)}
      {...props}
    >
      {defaultIcon && iconPosition !== 'none' && (
        <span className="flex items-center justify-center [&>svg]:size-3 [&>svg]:pointer-events-none">
          {defaultIcon}
        </span>
      )}
      {children}
    </Comp>
  );
}

export { Chip, chipVariants };
