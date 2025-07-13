import { KeywordChip } from '@/features/filtering/ui/KeywordFilterPreview';
import { KeywordType } from '@/features/filtering/hook/useFilter';

const toKeywordChips = <T extends keyof KeywordType>(
  labels: string[],
  type: T
): KeywordChip[] => labels.map(label => ({ label, type }));
export default toKeywordChips;
