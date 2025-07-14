'use client';

import BottomSheet from '@/shared/ui/bottomSheet/bottomSheet';
import OptionTitle from '@/features/filtering/ui/OptionTitle';
import { MonthlyCalendar } from '@/shared/ui';
import LocationOption from '@/features/filtering/ui/LocationOption';
import KeywordOption from '@/features/filtering/ui/KeywordOption';
import { useFilterContext } from '@/features/filtering/lib/FilterContext';

export default function FilterBottomSheet() {
  const {
    isOpen,
    openType,
    handleClose,
    handleApply,
    handleReset,
    handleFilterChange,
    tempState,
    isApplyDisabled,
    handleDeleteKeyword,
  } = useFilterContext();

  if (!openType) return null;

  const renderContent = () => {
    switch (openType) {
      case 'date':
        return (
          <MonthlyCalendar
            mode={'range'}
            selected={tempState.date}
            onSelected={value => handleFilterChange('date', value)}
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

  return (
    <BottomSheet
      isOpen={isOpen}
      onClose={handleClose}
      handleReset={() => handleReset(openType)}
      handleApply={handleApply}
      drawerTitle={OptionTitle({ openType })}
      applyDisabled={isApplyDisabled}
    >
      {renderContent()}
    </BottomSheet>
  );
}
