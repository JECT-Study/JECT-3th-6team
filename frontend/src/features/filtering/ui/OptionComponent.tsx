'use client';

import { MonthlyCalendar } from '@/shared/ui';
import LocationOption from './LocationOption';
import { useFilterContext } from '@/features/filtering/lib/FilterContext';
import { DateRange } from '@/shared/ui/calendar/MonthlyCalendar';
import KeywordOption from '@/features/filtering/ui/KeywordOption';

const OptionComponent = () => {
  const { handleFilterChange, tempState, handleDeleteKeyword, openType } =
    useFilterContext();

  switch (openType) {
    case 'date':
      return (
        <MonthlyCalendar
          mode={'range'}
          selected={tempState.date}
          onSelect={(value: DateRange) => handleFilterChange('date', value)}
        />
      );
    case 'location':
      return (
        <LocationOption
          selected={tempState.location}
          onSelect={value => handleFilterChange('location', value)}
        />
      );
    case 'keyword':
      return (
        <KeywordOption
          selected={tempState.keyword}
          onSelect={value => handleFilterChange('keyword', value)}
          onDelete={({ label, type }) =>
            handleDeleteKeyword(label, type, 'temp')
          }
        />
      );
    default:
      return null;
  }
};

export default OptionComponent;
