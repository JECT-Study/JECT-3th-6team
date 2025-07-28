import { DateRange } from '@/shared/ui/calendar/MonthlyCalendar';

export type FilterType = 'date' | 'keyword' | 'location';
export type KeywordType = {
  popupType: Array<string>;
  category: Array<string>;
};

export type FilterValueMap = {
  date: DateRange;
  keyword: KeywordType;
  location: string;
};

export type FilterState = {
  [K in FilterType]: FilterValueMap[K];
};
export type FilterValue<T extends FilterType> = FilterValueMap[T];
