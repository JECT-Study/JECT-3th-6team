import { ChangeEvent } from 'react';
import { TextInputProps } from '@/shared/ui/input/types';

export default function TextInput({
  label,
  value,
  onChange,
  placeholder,
  error = false,
  errorMessage = '',
  disabled = false,
  type = 'text',
  inputMode = 'text',
  id = '',
}: TextInputProps) {
  const borderStyle = error ? 'border-red' : 'border-gray40';

  return (
    <div className={'flex flex-col gap-2 w-full'}>
      <label
        htmlFor={id}
        className={'font-semibold text-sm/normal tracking-wide select-none'}
      >
        {label}
      </label>
      <input
        id={id}
        value={value}
        onChange={(e: ChangeEvent<HTMLInputElement>) =>
          onChange(e.target.value)
        }
        placeholder={placeholder}
        disabled={disabled}
        type={type}
        inputMode={inputMode}
        className={`px-3 py-3 rounded-xl border text-base font-regular/normal tracking-wide placeholder:text-gray40 text-gray80 focus:outline-none caret-main focus:border-main ${borderStyle}`}
      />
      {error && (
        <span className={'font-regular text-xs/normal text-red tracking-wide'}>
          *{errorMessage}
        </span>
      )}
    </div>
  );
}
