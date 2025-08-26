import PopupCardSkeleton from '@/entities/popup/ui/PopupCardSkeleton';

export default function PopupCardListSuspenseFallback() {
  return (
    <div className="flex flex-col gap-4 bg-white px-5 pt-4">
      {Array.from({ length: 6 }).map((_, index) => (
        <PopupCardSkeleton key={index} />
      ))}
    </div>
  );
}
