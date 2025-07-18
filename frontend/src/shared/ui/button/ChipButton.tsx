import { tv } from 'tailwind-variants';
import React from 'react';

interface ChipButtonProps extends React.HTMLAttributes<HTMLButtonElement> {
  label: string;
  isChecked: boolean;
  disabled: boolean;
  onChipClick: () => void;
  shape?: 'square' | 'round';
  color?: 'primary' | 'secondary' | 'white' | 'transparent';
}

export default function ChipButton({
  label,
  isChecked,
  disabled,
  onChipClick,
  shape = 'round',
  color = 'transparent',
  ...props
}: ChipButtonProps) {
  const buttonStyle = tv({
    base: ' text-base font-regular',
    variants: {
      color: {
        primary: 'bg-main text-white',
        secondary: 'bg-sub-pale text-black',
        white: 'bg-white text-gray60 border border-gray40',
        transparent: 'bg-transparent text-black ',
      },
      shape: {
        round: 'rounded-full py-[3px] px-3.5  h-[36px] border border-gray40',
        square:
          'rounded-[4px] w-[76px] h-[40px] flex justify-center items-center',
      },
      isChecked: {
        true: 'bg-main text-white shadow-button',
      },
      disabled: {
        true: 'bg-gray40 text-gray80',
      },
    },
    compoundVariants: [
      {
        shape: 'round',
        isChecked: true,
        class: 'shadow-none',
      },
    ],
  });

  return (
    <button
      type="button"
      onClick={onChipClick}
      className={buttonStyle({
        shape,
        isChecked,
        disabled,
        color,
      })}
      disabled={disabled}
      {...props}
    >
      {label}
    </button>
  );
}
