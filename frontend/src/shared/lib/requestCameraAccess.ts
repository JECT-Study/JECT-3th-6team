import {
  isCameraSupported,
  getCameraErrorMessage,
} from '@/shared/lib/cameraUtils';
import QrScanner from 'qr-scanner';

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
