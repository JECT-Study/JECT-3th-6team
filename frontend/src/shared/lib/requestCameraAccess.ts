import {
  isCameraSupported,
  getCameraErrorMessage,
  isMobileDevice,
} from '@/shared/lib/cameraUtils';
import QrScanner from 'qr-scanner';

/**
 * 모바일에서도 브라우저 카메라를 사용하되 더 안정적으로 QR 스캔합니다.
 */
const startMobileCameraWithQR = async (
  onQRDetected: (qrData: string) => void
): Promise<void> => {
  try {
    console.log('모바일 카메라를 시작합니다...');

    // 모바일에 최적화된 카메라 설정
    const stream = await navigator.mediaDevices.getUserMedia({
      video: {
        facingMode: { exact: 'environment' }, // 후면 카메라 강제
        width: { ideal: 1920, max: 1920 },
        height: { ideal: 1080, max: 1080 },
      },
      audio: false,
    });

    console.log('모바일 카메라 권한이 허용되었습니다.');

    // 전체화면 비디오 요소 생성
    const video = document.createElement('video');
    video.style.position = 'fixed';
    video.style.top = '0';
    video.style.left = '0';
    video.style.width = '100vw';
    video.style.height = '100vh';
    video.style.objectFit = 'cover';
    video.style.zIndex = '9999';
    video.style.backgroundColor = 'black';
    video.autoplay = true;
    video.playsInline = true;
    video.muted = true;

    // 비디오에 스트림 연결
    video.srcObject = stream;

    // 닫기 버튼 추가 (모바일용)
    const closeButton = document.createElement('button');
    closeButton.innerHTML = '✕';
    closeButton.style.position = 'fixed';
    closeButton.style.top = '20px';
    closeButton.style.right = '20px';
    closeButton.style.zIndex = '10000';
    closeButton.style.backgroundColor = 'rgba(0,0,0,0.7)';
    closeButton.style.color = 'white';
    closeButton.style.border = 'none';
    closeButton.style.borderRadius = '50%';
    closeButton.style.width = '50px';
    closeButton.style.height = '50px';
    closeButton.style.fontSize = '20px';
    closeButton.style.cursor = 'pointer';

    // QR 스캐너 설정
    const qrScanner = new QrScanner(
      video,
      result => {
        console.log('QR 코드 감지:', result.data);

        // 정리 작업
        qrScanner.stop();
        qrScanner.destroy();
        stream.getTracks().forEach(track => track.stop());

        if (document.body.contains(video)) {
          document.body.removeChild(video);
        }
        if (document.body.contains(closeButton)) {
          document.body.removeChild(closeButton);
        }

        // 콜백 호출
        onQRDetected(result.data);
      },
      {
        highlightScanRegion: true,
        highlightCodeOutline: true,
        maxScansPerSecond: 5, // 스캔 빈도 증가
      }
    );

    // 닫기 버튼 이벤트
    closeButton.onclick = () => {
      qrScanner.stop();
      qrScanner.destroy();
      stream.getTracks().forEach(track => track.stop());

      if (document.body.contains(video)) {
        document.body.removeChild(video);
      }
      if (document.body.contains(closeButton)) {
        document.body.removeChild(closeButton);
      }
    };

    // DOM에 추가
    document.body.appendChild(video);
    document.body.appendChild(closeButton);

    // 비디오 재생 및 스캐너 시작
    await video.play();
    await qrScanner.start();
    console.log('모바일 QR 스캐너가 시작되었습니다.');
  } catch (err) {
    console.error('모바일 카메라 시작 실패:', err);
    const errorMessage = getCameraErrorMessage(err as Error);
    throw new Error(errorMessage);
  }
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
 * - 모바일: 후면 카메라 우선, 고해상도 설정으로 QR 스캔 개선
 * - 웹: 브라우저 내 카메라로 실시간 QR 스캔
 */
export const requestCameraAccess = async (
  onQRDetected: (qrData: string) => void
): Promise<void> => {
  if (isMobileDevice()) {
    console.log('모바일 디바이스: 최적화된 카메라로 QR 스캔을 시작합니다.');
    await startMobileCameraWithQR(onQRDetected);
  } else {
    console.log('웹 브라우저: 브라우저 내 카메라로 QR 스캔을 시작합니다.');
    await startCameraWithQRDetection(onQRDetected);
  }
};
