import { TextProps } from './types';

export function MediumText({
  children,
  className = '',
  onClick,
  color,
  ...props
}: TextProps) {
  return (
    <p
      className={`text-[16px] font-medium leading-normal ${color} ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </p>
  );
}
