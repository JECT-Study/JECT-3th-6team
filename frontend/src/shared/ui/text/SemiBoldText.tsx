import { TextProps } from './types';

interface SemiBoldTextProps extends TextProps {
  size?: 'md' | 'lg'; // 20px | 24px
}

export function SemiBoldText({ 
  children, 
  size = 'md', // 기본값 20px
  className = '', 
  onClick,
  ...props 
}: SemiBoldTextProps) {
  const sizeClasses = {
    md: 'text-[20px]',
    lg: 'text-[24px]'
  };

  return (
    <p 
      className={`${sizeClasses[size]} font-semibold text-text-color leading-normal ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </p>
  );
} 