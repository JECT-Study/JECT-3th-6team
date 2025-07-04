import React from 'react';
import type { ButtonProps } from './types';

export default function StandardButton({
  children,
  onClick,
  disabled,
  color = 'white',
  size = 'full',
  customClass = '',
  hasShadow = false,
}: ButtonProps) {
  const colorMap = {
    primary: 'bg-main text-white',
    secondary: 'bg-sub-pale text-black',
    white: 'bg-white text-gray80 border border-gray40',
  };

  const sizeMap = {
    fit: 'min-w-fit w-max px-6 py-2.5 rounded-sm',
    full: 'w-full py-2.5 rounded-lg',
  };

  const shadowStyle = hasShadow ? 'shadow-button' : '';

  const style = colorMap[color] + ' ' + sizeMap[size] + ' ' + shadowStyle;

  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`text-normal font-medium cursor-pointer active:bg-main-active active:text-white disabled:bg-gray40 disabled:text-gray80 transition-colors duration-250 ${style} ${customClass}`}
    >
      {children}
    </button>
  );
}
