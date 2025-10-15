'use client';

import ModalContainer from '@/shared/ui/modal/ModalContainer';
import Button from '@/components/ui/button';
import { StandardButton } from '@/shared/ui';

interface WithdrawalConfirmModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
}

export default function WithdrawalConfirmModal({
  isOpen,
  onClose,
  onConfirm,
}: WithdrawalConfirmModalProps) {
  const handleConfirm = async () => {
    try {
      await onConfirm();
    } catch (error) {
      console.error('탈퇴 처리 중 오류 발생:', error);
    } finally {
      onClose();
    }
  };

  return (
    <ModalContainer open={isOpen}>
      <div className="bg-white rounded-lg p-6 max-w-sm w-full mx-4">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">
          정말 탈퇴하시겠습니까?
        </h2>
        <p className="text-sm text-gray-600 mb-6">
          탈퇴하면 모든 방문 내역이 삭제되며 복구되지 않습니다.
        </p>
        <div className="flex gap-3">
          <StandardButton
            color="white"
            onClick={onClose}
            className="flex-1 cursor-pointer"
          >
            아니오
          </StandardButton>
          <StandardButton
            color="primary"
            onClick={handleConfirm}
            className="flex-1 cursor-pointer"
          >
            네
          </StandardButton>
        </div>
      </div>
    </ModalContainer>
  );
}
