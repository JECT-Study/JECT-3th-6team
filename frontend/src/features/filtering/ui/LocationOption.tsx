import { ChipButton } from '@/shared/ui';
import LOCATION_OPTIONS from '@/features/filtering/lib/locationOptions';

interface LocationOptionProps {
  selected: string;
  onSelect: (location: string) => void;
}

export default function LocationOption({
  selected,
  onSelect,
}: LocationOptionProps) {
  return (
    <div
      className={
        'grid grid-cols-4 grid-rows-5 gap-y-[16px] bg-sub2 px-[16px] py-[20px] rounded-[10px] mb-[50px]'
      }
    >
      {LOCATION_OPTIONS.map((location, index) => {
        return (
          <ChipButton
            key={index}
            label={location}
            isChecked={selected === location}
            disabled={false}
            shape={'square'}
            onChipClick={() => onSelect(location)}
          />
        );
      })}
    </div>
  );
}
