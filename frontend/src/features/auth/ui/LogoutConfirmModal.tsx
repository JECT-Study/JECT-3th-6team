'use client';

import { StandardButton, ModalContainer } from '@/shared/ui';

import { useLogout } from '@/features/auth/hook/useLogout';

interface LogoutConfirmModalProps {
  open: boolean;
  onClose: () => void;
}

export const LogoutConfirmModal = ({
  open,
  onClose,
}: LogoutConfirmModalProps) => {
  const logout = useLogout();

  const handleConfirmLogout = () => {
    logout();
    onClose();
  };

  return (
    <ModalContainer open={open}>
      <div className="bg-white rounded-3xl p-9 w-[335px] h-[168px] shadow-lg">
        <div className="text-center">
          <p className="text-gray-900 text-base font-medium mb-6">
            스팟잇에서 로그아웃 하시겠습니까?
          </p>

          <div className="flex gap-3">
            <StandardButton
              onClick={onClose}
              color="white"
              className="flex-1"
              disabled={false}
            >
              아니요
            </StandardButton>

            <StandardButton
              onClick={handleConfirmLogout}
              color="primary"
              className="flex-1"
              disabled={false}
            >
              네
            </StandardButton>
          </div>
        </div>
      </div>
    </ModalContainer>
  );
};
