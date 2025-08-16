import {
  Drawer,
  DrawerContent,
  DrawerFooter,
  DrawerHeader,
  DrawerTitle,
} from '@/components/ui/drawer';
import { StandardButton } from '@/shared/ui';
import IconReload from '@/assets/icons/Normal/Icon_Reload.svg';
import React from 'react';

interface BottomSheetProps {
  isOpen: boolean;
  onClose: () => void;
  handleReset: () => void;
  handleApply: () => void;
  applyDisabled: boolean;
  drawerTitle?: React.ReactNode;
  children: React.ReactNode;
}

export default function BottomSheet({
  isOpen,
  onClose,
  handleReset,
  handleApply,
  applyDisabled,
  drawerTitle,
  children,
}: BottomSheetProps) {
  return (
    <Drawer open={isOpen} onClose={onClose}>
      <DrawerContent
        className={'min-w-[320px] max-w-[430px] mx-auto  max-h-[80dvh] w-full'}
      >
        <DrawerHeader className="shrink-0">
          <DrawerTitle>{drawerTitle}</DrawerTitle>
        </DrawerHeader>
        <div
          className={'px-4 flex-1 min-h-0 overflow-y-auto overscroll-contain'}
        >
          {children}
        </div>
        <DrawerFooter className="shrink-0 pb-[calc(env(safe-area-inset-bottom)+12px)]">
          <div className={'flex justify-between gap-x-2'}>
            <StandardButton
              onClick={handleReset}
              disabled={false}
              size={'fit'}
              color={'white'}
              hasShadow={false}
              className={'rounded-[10px]'}
            >
              <div className={'flex items-center gap-x-2'}>
                <IconReload
                  width={17}
                  height={17}
                  fill={'var(--color-gray60)'}
                />
                <span>초기화</span>
              </div>
            </StandardButton>

            <StandardButton
              onClick={handleApply}
              disabled={applyDisabled}
              size={'full'}
              color={'primary'}
              hasShadow={false}
            >
              적용하기
            </StandardButton>
          </div>
        </DrawerFooter>
      </DrawerContent>
    </Drawer>
  );
}
