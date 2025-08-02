import IconMap from '@/assets/icons/Normal/Icon_map.svg';

interface FallbackProps {
  type: 'DEFAULT' | 'HISTORY';
}

const messageMap = {
  DEFAULT: {
    info: '검색된 팝업이 없어요',
    retry: '다른 지역이나 키워드로 다시 검색해보세요.',
  },
  HISTORY: {
    info: '방문내역이 없어요',
    retry: '새로운 팝업 스토어를 방문해보세요',
  },
};

export default function EmptyPopupListFallback({ type }: FallbackProps) {
  return (
    <div className="flex flex-col items-center justify-center text-center py-16 px-4 text-gray-500">
      <IconMap width={150} height={150} fill={'var(--color-gray40)'} />
      <p className="text-base font-medium text-black select-none">
        {messageMap[type].info}
      </p>
      <p className="mt-2 text-sm text-gray80 select-none">
        {messageMap[type].retry}{' '}
      </p>
    </div>
  );
}
