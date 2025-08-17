import {
  isCameraSupported,
  getCameraErrorMessage,
  isMobileDevice,
} from '@/shared/lib/cameraUtils';
import QrScanner from 'qr-scanner';

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
 * 웹에서 브라우저 카메라를 시작하고 QR 코드를 감지합니다.
 */
const startCameraWithQRDetection = async (
  onQRDetected: (qrData: string) => void
): Promise<void> => {
  try {
    // 브라우저 지원 체크
    if (!isCameraSupported()) {
      throw new Error('이 브라우저는 카메라 기능을 지원하지 않습니다.');
    }

    // 먼저 카메라 권한 요청
    console.log('카메라 권한을 요청합니다...');
    const stream = await navigator.mediaDevices.getUserMedia({
      video: {
        facingMode: 'environment', // 후면 카메라 우선
      },
      audio: false,
    });

    console.log('카메라 권한이 허용되었습니다.');

    // 임시 비디오 요소 생성
    const video = document.createElement('video');
    video.style.position = 'fixed';
    video.style.top = '0';
    video.style.left = '0';
    video.style.width = '100vw';
    video.style.height = '100vh';
    video.style.objectFit = 'cover';
    video.style.zIndex = '9999';
    video.autoplay = true;
    video.playsInline = true;
    video.muted = true;

    // 비디오에 스트림 연결
    video.srcObject = stream;

    // QR 스캐너 설정
    const qrScanner = new QrScanner(
      video,
      result => {
        console.log('QR 코드 감지:', result.data);

        // QR 스캐너 정리
        qrScanner.stop();
        qrScanner.destroy();

        // 스트림 정리
        stream.getTracks().forEach(track => track.stop());

        if (document.body.contains(video)) {
          document.body.removeChild(video);
        }

        // 콜백 호출
        onQRDetected(result.data);
      },
      {
        highlightScanRegion: true,
        highlightCodeOutline: true,
      }
    );

    // DOM에 비디오 추가
    document.body.appendChild(video);

    // 비디오 재생 시작
    await video.play();

    // QR 스캐너 시작 (이미 스트림이 있으므로 start 대신 hasCamera 설정)
    await qrScanner.start();
    console.log('QR 스캐너가 시작되었습니다.');
  } catch (err) {
    const errorMessage = getCameraErrorMessage(err as Error);
    throw new Error(errorMessage);
  }
};

/**
 * 카메라를 시작하고 QR 코드를 감지합니다.
 * QR 코드가 감지되면 콜백이 호출됩니다.
 */
export const requestCameraAccess = async (
  onQRDetected: (qrData: string) => void
): Promise<void> => {
  console.log('카메라를 시작하고 QR 스캔을 준비합니다.');
  await startCameraWithQRDetection(onQRDetected);
};
