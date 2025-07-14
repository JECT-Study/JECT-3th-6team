interface ChipButtonProps {
  label: string;
  value: string;
  isChecked: boolean;
  disabled: boolean;
  onChange: (value: string) => void;
}

export default function ChipButton({
  label,
  value,
  isChecked,
  disabled,
  onChange,
}: ChipButtonProps) {
  const buttonStyle = isChecked
    ? 'bg-main text-white'
    : 'bg-white border border-gray40 text-gray60';
  return (
    <button
      type="button"
      onClick={() => onChange(value)}
      className={`w-[78px] h-[36px] py-[3px] px-3.5 rounded-full text-base font-medium ${buttonStyle} 
        disabled:bg-gray40 disabled:text-gray80`}
      disabled={disabled}
    >
      {label}
    </button>
  );
}
