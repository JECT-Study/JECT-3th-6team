export default function PopupCardSkeleton() {
  return (
    <div className="relative w-full flex rounded-2xl bg-white overflow-hidden shadow-card border border-gray40 animate-pulse">
      {/* Image Skeleton */}
      <div className="relative w-[140px] min-h-[144px] bg-gray-200" />

      {/* Content Skeleton */}
      <div className="relative flex flex-1 flex-col justify-between py-3 pl-4">
        {/* Location */}
        <div className="flex flex-col gap-y-[3px]">
          <div className="flex items-center gap-x-1">
            <div className="w-[20px] h-[20px] bg-gray-300 rounded-full" />
            <div className="h-[14px] w-[100px] bg-gray-300 rounded" />
          </div>

          {/* Title */}
          <div className="h-[16px] w-[180px] bg-gray-300 rounded" />

          {/* Tag Line */}
          <div className="h-[14px] w-[140px] bg-gray-200 rounded" />
        </div>

        {/* Date Range */}
        <div className="w-[120px] h-[14px] bg-gray-200 rounded mt-2" />
      </div>
    </div>
  );
}
