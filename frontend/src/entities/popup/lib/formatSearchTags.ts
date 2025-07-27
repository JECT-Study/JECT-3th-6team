import { searchTagType } from '@/entities/popup/types/PopupListItem';

const TypeTagMap = {
  EXPERIENTIAL: '체험형',
};

export function formatSearchTags(tags: searchTagType): string {
  const { type, category } = tags;
  // TODO : 체험형, 전시형으로 오는 키워드의 코드 정보 필요
  return [`#${TypeTagMap[type]}`, ...category.map(item => `#${item}`)].join(
    ' '
  );
}
