import {
  isCameraSupported,
  getCameraErrorMessage,
  isMobileDevice,
} from '@/shared/lib/cameraUtils';
import QrScanner from 'qr-scanner';

/**
 * 모바일에서 기본 카메라 앱을 열어 QR 코드를 스캔합니다.
 */
const openMobileCameraForQR = (
  onQRDetected: (qrData: string) => void
): Promise<void> => {
  return new Promise((resolve, reject) => {
    try {
      // 숨겨진 file input 요소 생성
      const input = document.createElement('input');
      input.type = 'file';
      input.accept = 'image/*';
      input.capture = 'environment'; // 후면 카메라 사용
      input.style.display = 'none';

      input.addEventListener('change', async event => {
        const target = event.target as HTMLInputElement;
        if (target.files && target.files.length > 0) {
          const file = target.files[0];
          console.log('모바일 카메라에서 이미지가 촬영되었습니다.');

          try {
            // QR 코드 스캔 시도
            const qrData = await QrScanner.scanImage(file);
            console.log('QR 코드 감지:', qrData);
            onQRDetected(qrData);
            resolve();
          } catch (error) {
            console.log('QR 코드를 찾을 수 없습니다:', error);
            alert('QR 코드를 인식할 수 없습니다. 다시 촬영해주세요.');
            resolve(); // 에러여도 resolve (사용자가 다시 시도할 수 있도록)
          }
        }

        // input 요소 제거
        if (document.body.contains(input)) {
          document.body.removeChild(input);
        }
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
      console.log('모바일 카메라를 열 수 없습니다:', error);
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
 * - 모바일: 기본 카메라 앱을 열어 사진 촬영 후 QR 인식
 * - 웹: 브라우저 내 카메라로 실시간 QR 스캔
 */
export const requestCameraAccess = async (
  onQRDetected: (qrData: string) => void
): Promise<void> => {
  if (isMobileDevice()) {
    console.log('모바일 디바이스: 기본 카메라 앱을 엽니다.');
    await openMobileCameraForQR(onQRDetected);
  } else {
    console.log('웹 브라우저: 브라우저 내 카메라로 QR 스캔을 시작합니다.');
    await startCameraWithQRDetection(onQRDetected);
  }
};
