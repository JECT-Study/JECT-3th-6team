/**
 * 주어진 ISO 8601 string 을 현재시간과의 시간차 string 으로 포맷팅합니다.
 *
 * @param {string} isoString - The ISO 8601 formatted date string
 * @return {string} "just now", "5 mins ago", or "2 days ago"와 같은 시간차
 */

export default function formatRelativeTime(isoString: string): string {
  const now = new Date();
  const target = new Date(isoString);
  const diffMs = now.getTime() - target.getTime();

  const diffSec = Math.floor(diffMs / 1000);
  const diffMin = Math.floor(diffSec / 60);
  const diffHour = Math.floor(diffMin / 60);
  const diffDay = Math.floor(diffHour / 24);

  if (diffMin < 1) return 'just now';
  if (diffMin < 60) return `${diffMin} mins ago`;
  if (diffHour < 24) return `${diffHour} hours ago`;
  if (diffDay < 7) return `${diffDay} days ago`;
  else return `${diffDay} days ago`;
}
