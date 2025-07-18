'use client';

import BottomSheet from '@/shared/ui/bottomSheet/bottomSheet';
import OptionTitle from '@/features/filtering/ui/OptionTitle';
import { useFilterContext } from '@/features/filtering/lib/FilterContext';
import OptionComponent from '@/features/filtering/ui/OptionComponent';

export default function FilterBottomSheet() {
  const {
    isOpen,
    openType,
    handleClose,
    handleApply,
    handleReset,
    isApplyDisabled,
  } = useFilterContext();

  if (!openType) return null;

  return (
    <BottomSheet
      isOpen={isOpen}
      onClose={handleClose}
      handleReset={() => handleReset(openType)}
      handleApply={handleApply}
      drawerTitle={OptionTitle({ openType })}
      applyDisabled={isApplyDisabled}
    >
      <OptionComponent />
    </BottomSheet>
  );
}
