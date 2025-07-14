import React from 'react';
import { cn } from '@/lib/utils';
import IconBracketDown from '@/assets/icons/Normal/Icon_Bracket_Down.svg';
interface SelectButtonProps extends React.HTMLAttributes<HTMLDivElement> {
  Icon: React.ReactNode;
  onClick?: () => void;
  label: string;
  className?: string;
}

export default function SelectButton({
  Icon,
  onClick,
  label,
  className,
  ...props
}: SelectButtonProps) {
  return (
    <div
      {...props}
      onClick={onClick}
      className={cn(
        'flex items-center w-full justify-between bg-white rounded-[6px] px-[10px] gap-x-[10px] py-[8px] cursor-pointer hover:bg-gray-100',
        className
      )}
    >
      <>{Icon}</>
      <span className="text-gray60 font-semibold text-sm flex-1 ">{label}</span>
      <IconBracketDown width={22} height={22} fill={'var(--color-gray60)'} />
    </div>
  );
}
