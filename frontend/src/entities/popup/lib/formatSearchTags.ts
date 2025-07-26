import { searchTagType } from '@/entities/popup/types/PopupListItem';

export function formatSearchTags(tags: searchTagType): string {
  const { type, category } = tags;
  return [`#${type}`, ...category.map(item => `#${item}`)].join(' ');
}
