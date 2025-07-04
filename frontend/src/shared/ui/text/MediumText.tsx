import { TextProps } from './types';

export function MediumText({ 
  children, 
  className = '', 
  onClick,
  color,
  ...props 
}: TextProps) {
  const textColorClass = color || 'text-text-color';
  
  return (
    <p 
      className={`text-[16px] font-medium ${textColorClass} leading-normal ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </p>
  );
}   