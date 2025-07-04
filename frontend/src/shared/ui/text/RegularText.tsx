import { TextProps } from './types';

interface RegularTextProps extends TextProps {
  size?: 'sm' | 'md'; // 12px | 14px
}

export function RegularText({ 
  children, 
  size = 'md', // 기본값 14px
  className = '', 
  onClick,
  ...props 
}: RegularTextProps) {
  const sizeClasses = {
    sm: 'text-[12px]',
    md: 'text-[14px]'
  };

  return (
    <p 
      className={`${sizeClasses[size]} font-normal text-text-color leading-normal ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </p>
  );
}   
