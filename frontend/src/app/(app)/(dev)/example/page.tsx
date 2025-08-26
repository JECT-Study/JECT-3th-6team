import { RegularText } from '@/shared/ui/text/RegularText';
import { TagExample } from '@/shared/ui/tag/TagExample';
import { BadgeExample } from '@/shared/ui/badge/BadgeExample';
import { ChipExample } from '@/shared/ui/chip/ChipExample';
import { MediumText } from '@/shared/ui/text/MediumText';
import { SemiBoldText } from '@/shared/ui/text/SemiBoldText';

export default function Home() {
  return (
    <div>
      <div className="p-4">
        <RegularText>regular text</RegularText>
        <MediumText>medium text</MediumText>
        <SemiBoldText>semi bold text</SemiBoldText>
      </div>
      <TagExample />
      <BadgeExample />
      <ChipExample />
    </div>
  );
}
