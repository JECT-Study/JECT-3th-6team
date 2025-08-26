import IconMinus from '@/assets/icons/Normal/Icon_Minus.svg';
import IconPlus from '@/assets/icons/Normal/Icon_Plus.svg';
import { NumberInputProps } from '@/shared/ui/input/types';

export default function NumberInput({
  value,
  label,
  max,
  min,
  onChange,
  errorMessage = '',
}: NumberInputProps) {
  const onMinusClick = () => {
    if (value > min) {
      onChange(value - 1);
    }
  };

  const onPlusClick = () => {
    if (value < max) {
      onChange(value + 1);
    }
  };

  return (
    <div className={'flex flex-col gap-y-0.5'}>
      <div className={'flex w-full items-center justify-between'}>
        <label className={'font-semibold text-sm tracking-[1.45%] select-none'}>
          {label}
        </label>
        <div
          className={
            'flex items-center justify-center w-40 h-12 gap-x-7 rounded-xl border border-gray40'
          }
        >
          {/*마이너스 버튼*/}
          <button
            type={'button'}
            className={'cursor-pointer'}
            onClick={onMinusClick}
          >
            <IconMinus width={29} height={29} fill={'var(--color-gray60)'} />
          </button>

          <span className={`text-xl min-w-3.5 font-regular text-black`}>
            {value}
          </span>

          {/*플러스버튼*/}
          <button
            type={'button'}
            className={'cursor-pointer'}
            onClick={onPlusClick}
          >
            <IconPlus width={29} height={29} fill={'var(--color-gray60)'} />
          </button>
        </div>
      </div>
      {errorMessage && (
        <span className={'font-regular text-xs text-red'}>*{errorMessage}</span>
      )}
    </div>
  );
}
