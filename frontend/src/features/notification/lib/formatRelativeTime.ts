export default function formatRelativeTime(isoString: string): string {
  const now = new Date();
  const target = new Date(isoString);
  const diffMs = now.getTime() - target.getTime();

  const diffSec = Math.floor(diffMs / 1000);
  const diffMin = Math.floor(diffSec / 60);
  const diffHour = Math.floor(diffMin / 60);
  const diffDay = Math.floor(diffHour / 24);
  const diffWeek = Math.floor(diffDay / 7);

  const yesterday = new Date();
  yesterday.setDate(now.getDate() - 1);
  const isYesterday = target.toDateString() === yesterday.toDateString();

  if (diffMin < 1) return '방금';
  if (diffMin < 60) return `${diffMin}분 전`;
  if (diffHour < 24) return `${diffHour}시간 전`;
  if (isYesterday) return '어제';
  if (diffDay < 7) return `${diffDay}일 전`;
  return `${diffWeek}주 전`;
}
