import React from 'react';
import type { ButtonProps, ColorType, SizeType } from './types';
import { cn } from '@/lib/utils';

export default function StandardButton({
  children,
  onClick,
  disabled,
  color = 'white',
  size = 'full',
  hasShadow = false,
  className,
  ...props
}: ButtonProps) {
  const colorMap: Record<ColorType, string> = {
    primary: 'bg-main text-white',
    secondary: 'bg-sub-pale text-black',
    white: 'bg-white text-gray80 border border-gray40',
  };
  const sizeMap: Record<SizeType, string> = {
    fit: 'min-w-fit w-max px-6 py-2.5 rounded-sm',
    full: 'w-full py-2.5 rounded-lg',
  };

  const shadowStyle = hasShadow ? 'shadow-button' : '';
  const baseStyle =
    'text-normal font-medium cursor-pointer active:bg-main-active active:text-white disabled:bg-gray40 disabled:text-gray80 disabled:cursor-default transition-colors duration-250';

  return (
    <button
      type={'button'}
      onClick={onClick}
      disabled={disabled}
      className={cn(
        baseStyle,
        colorMap[color],
        sizeMap[size],
        shadowStyle,
        className
      )}
      {...props}
    >
      {children}
    </button>
  );
}
