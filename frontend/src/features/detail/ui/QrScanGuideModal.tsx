'use client';

import StandardButton from '@/shared/ui/button/StandardButton';
import { SemiBoldText } from '@/shared/ui/text/SemiBoldText';
import ModalContainer from '@/shared/ui/modal/ModalContainer';

interface QrScanGuideModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function QrScanGuideModal({
  isOpen,
  onClose,
}: QrScanGuideModalProps) {
  return (
    <ModalContainer open={isOpen}>
      <div className="bg-white rounded-2xl p-6 mx-4 max-w-sm w-full">
        {/* 모달 내용 */}
        <div className="text-center mb-8">
          <SemiBoldText size="lg" color="color-black">
            카메라를 켜서 QR코드를 인식해주세요
          </SemiBoldText>
        </div>

        {/* 버튼 영역 */}
        <div className="flex justify-center">
          <StandardButton
            onClick={onClose}
            color="primary"
            className="w-full"
            disabled={false}
          >
            확인
          </StandardButton>
        </div>
      </div>
    </ModalContainer>
  );
}
