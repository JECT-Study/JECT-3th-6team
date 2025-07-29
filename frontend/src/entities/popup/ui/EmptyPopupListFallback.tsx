import IconReload from '@/assets/icons/Normal/Icon_Reload.svg';

export default function EmptyPopupListFallback() {
  return (
    <div className="flex flex-col items-center justify-center text-center py-16 px-4 text-gray-500">
      <IconReload width={40} height={40} fill={'var(--color-gray80)'} />
      <p className="text-lg font-semibold">검색된 팝업이 없어요</p>
      <p className="mt-2 text-sm text-gray-400">
        다른 지역이나 키워드로 다시 검색해보세요.
      </p>
    </div>
  );
}
