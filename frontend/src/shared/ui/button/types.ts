import React from 'react';

export type ColorType = 'primary' | 'secondary' | 'white';
export type SizeType = 'fit' | 'full';

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
  onClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
  disabled: boolean;
  color?: ColorType;
  size?: SizeType;
  hasShadow?: boolean;
}
