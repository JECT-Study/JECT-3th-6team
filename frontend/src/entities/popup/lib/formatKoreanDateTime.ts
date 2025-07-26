export function formatKoreanDateTime(isoString: string): string {
  const date = new Date(isoString);

  const year = date.getFullYear();
  const month = date.getMonth() + 1; // 0-based
  const day = date.getDate();

  let hour = date.getHours();
  const minute = date.getMinutes().toString().padStart(2, '0');

  const isAM = hour < 12;
  const period = isAM ? '오전' : '오후';

  // 12시간제로 변경
  hour = hour % 12;
  hour = hour === 0 ? 12 : hour;

  return `${period} ${hour}:${minute} • ${year}/${month}/${day}`;
}
