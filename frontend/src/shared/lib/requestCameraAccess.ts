import {
  isCameraSupported,
  getCameraErrorMessage,
  isMobileDevice,
} from './cameraUtils';

/**
 * 모바일에서 기본 카메라 앱을 열기 위한 파일 input 요소를 생성합니다.
 */
const openMobileCamera = (): Promise<void> => {
  return new Promise((resolve, reject) => {
    try {
      // 숨겨진 file input 요소 생성
      const input = document.createElement('input');
      input.type = 'file';
      input.accept = 'image/*';
      input.capture = 'environment'; // 후면 카메라 사용
      input.style.display = 'none';

      input.addEventListener('change', event => {
        const target = event.target as HTMLInputElement;
        if (target.files && target.files.length > 0) {
          console.log('모바일 카메라에서 이미지가 촬영되었습니다.');
          resolve();
        }

        document.body.removeChild(input);
      });

      const handleCancel = () => {
        console.log('카메라 촬영이 취소되었습니다.');
        if (document.body.contains(input)) {
          document.body.removeChild(input);
        }
        resolve();
      };

      // focus가 돌아왔을 때 취소로 간주 (fallback)
      const handleFocus = () => {
        setTimeout(() => {
          if (document.body.contains(input)) {
            handleCancel();
            window.removeEventListener('focus', handleFocus);
          }
        }, 1000);
      };

      input.addEventListener('cancel', handleCancel);
      window.addEventListener('focus', handleFocus);

      // DOM에 추가하고 클릭 트리거
      document.body.appendChild(input);
      input.click();
    } catch (error) {
      console.log('모바일 카메라를 열 수 없습니다.', error);
      reject(new Error('모바일 카메라를 열 수 없습니다.'));
    }
  });
};

/**
 * 웹에서 브라우저 카메라 권한을 요청합니다.
 */
const requestWebCameraAccess = async (): Promise<void> => {
  try {
    // 브라우저 지원 체크
    if (!isCameraSupported()) {
      throw new Error('이 브라우저는 카메라 기능을 지원하지 않습니다.');
    }

    // 카메라 권한 요청 및 접근
    const stream = await navigator.mediaDevices.getUserMedia({
      video: true,
      audio: false,
    });

    // 권한 획득 성공
    console.log('웹 카메라 권한이 허용되었습니다.');

    // 스트림 즉시 해제 (QR 코드에서 처리)
    stream.getTracks().forEach(track => track.stop());
  } catch (err) {
    const errorMessage = getCameraErrorMessage(err as Error);
    throw new Error(errorMessage);
  }
};

/**
 * 카메라 접근을 요청합니다.
 * - 모바일: 기본 카메라 앱이 열림
 * - 웹: 브라우저 내 카메라 권한 요청
 */
export const requestCameraAccess = async (): Promise<void> => {
  if (isMobileDevice()) {
    console.log('모바일 디바이스: 기본 카메라 앱을 엽니다.');
    await openMobileCamera();
  } else {
    console.log('웹 브라우저: 카메라 권한을 요청합니다.');
    await requestWebCameraAccess();
  }
};
