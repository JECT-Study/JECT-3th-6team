import { ChangeEvent, KeyboardEvent } from 'react';
import SearchIcon from '@/assets/icons/Normal/Icon_Search.svg';

interface SearchInputProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  disabled?: boolean;
  id?: string;
  onFocus?: () => void;
  onBlur?: () => void;
  onKeyDown?: (event: KeyboardEvent<HTMLInputElement>) => void;
}

export default function SearchInput({
  value,
  onChange,
  placeholder = '어떤 팝업을 찾으시나요?',
  disabled = false,
  id = '',
  onFocus,
  onBlur,
  onKeyDown,
}: SearchInputProps) {
  return (
    <div className="relative w-full">
      <div className="absolute left-3 top-1/2 transform -translate-y-1/2 flex items-center justify-center mt-1">
        <SearchIcon width={28} height={28} />
      </div>
      <input
        id={id}
        value={value}
        onChange={(e: ChangeEvent<HTMLInputElement>) =>
          onChange(e.target.value)
        }
        onFocus={onFocus}
        onBlur={onBlur}
        onKeyDown={onKeyDown}
        placeholder={placeholder}
        disabled={disabled}
        className="w-full h-[48px] px-3 pl-11 rounded-xl border border-main bg-white text-base font-regular tracking-wide placeholder:text-gray60 text-gray80 focus:outline-none caret-main"
      />
    </div>
  );
}
