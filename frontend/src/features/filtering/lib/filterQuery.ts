import { FilterState } from '../hook/type';
import { DateRange } from '@/shared/ui/calendar/MonthlyCalendar';

// 날짜를 문자열로 변환
const toISO = (d: Date | null) => {
  if (!d) return '';
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${y}-${m}-${day}`;
};

// 문자열을 날짜로 변환
const fromISO = (s: string | null): Date | null => {
  if (!s) return null;
  const [y, m, d] = s.split('-').map(Number);
  if (!y || !m || !d) return null;
  return new Date(y, m - 1, d);
};

// 기본값은 쿼리에서 생략(짧은 URL)
export const toQuery = (f: FilterState): string => {
  const sp = new URLSearchParams();

  // date
  const sd = toISO(f.date.start);
  const ed = toISO(f.date.end);
  if (sd) sp.set('sd', sd);
  if (ed) sp.set('ed', ed);

  // keyword (반복 키)
  f.keyword.popupType?.forEach(v => v && sp.append('pt', v));
  f.keyword.category?.forEach(v => v && sp.append('cat', v));

  // location
  if (f.location) sp.set('loc', f.location.trim());

  return sp.toString();
};

export const fromQuery = (
  sp: URLSearchParams,
  base: FilterState
): FilterState => {
  const next: FilterState = structuredClone(base);

  // date
  const start = fromISO(sp.get('sd'));
  const end = fromISO(sp.get('ed'));
  if (start || end) {
    next.date = { start, end } as DateRange;
  }

  // keyword
  const pt = sp.getAll('pt').filter(Boolean);
  const cat = sp.getAll('cat').filter(Boolean);
  if (pt.length) next.keyword.popupType = pt;
  if (cat.length) next.keyword.category = cat;

  // location
  const loc = sp.get('loc');
  if (loc !== null) next.location = loc;

  return next;
};

// 비교 안정화를 위한 정규화
// 날짜 -> 문자열
// 배열 정렬
export const normalize = (f: FilterState) => ({
  sd: toISO(f.date.start),
  ed: toISO(f.date.end),
  pt: [...(f.keyword.popupType ?? [])].sort(),
  cat: [...(f.keyword.category ?? [])].sort(),
  loc: (f.location ?? '').trim(),
});
export const isEqualFilter = (a: FilterState, b: FilterState) =>
  JSON.stringify(normalize(a)) === JSON.stringify(normalize(b));
