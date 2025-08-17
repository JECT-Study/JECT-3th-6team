/**
 * 카메라 관련 유틸리티 함수들
 */

/**
 * 브라우저가 카메라 API를 지원하는지 확인
 */
export const isCameraSupported = (): boolean => {
  return !!(
    navigator.mediaDevices &&
    navigator.mediaDevices.getUserMedia &&
    window.MediaStream
  );
};

/**
 * 모바일 디바이스인지 확인
 */
export const isMobileDevice = (): boolean => {
  const userAgent = navigator.userAgent.toLowerCase();
  return /mobile|android|iphone|ipad|ipod|blackberry|iemobile|opera mini/.test(
    userAgent
  );
};

/**
 * iOS Safari 여부 확인
 */
export const isIOSSafari = (): boolean => {
  const userAgent = navigator.userAgent.toLowerCase();
  return /iphone|ipad|ipod/.test(userAgent) && /safari/.test(userAgent);
};

/**
 * 브라우저별 최적화된 카메라 제약 조건 반환
 */
export const getOptimizedCameraConstraints = (): MediaStreamConstraints => {
  const baseConstraints: MediaStreamConstraints = {
    video: {
      width: { ideal: 1280, max: 1920 },
      height: { ideal: 720, max: 1080 },
      aspectRatio: { ideal: 16 / 9 },
      frameRate: { ideal: 30, max: 30 },
    },
    audio: false,
  };

  // 모바일 디바이스인 경우 후면 카메라 우선
  if (isMobileDevice()) {
    const videoConstraints = baseConstraints.video as MediaTrackConstraints;
    videoConstraints.facingMode = 'environment';

    // iOS Safari의 경우 더 제한적인 설정
    if (isIOSSafari()) {
      videoConstraints.width = { ideal: 720, max: 1280 };
      videoConstraints.height = { ideal: 480, max: 720 };
    }
  }

  return baseConstraints;
};

/**
 * 카메라 에러 메시지를 사용자 친화적으로 변환
 */
export const getCameraErrorMessage = (error: Error): string => {
  switch (error.name) {
    case 'NotAllowedError':
      return '카메라 권한이 거부되었습니다. 브라우저 설정에서 카메라 권한을 허용해주세요.';
    case 'NotFoundError':
      return '카메라를 찾을 수 없습니다. 디바이스에 카메라가 연결되어 있는지 확인해주세요.';
    case 'NotReadableError':
      return '카메라가 다른 애플리케이션에서 사용중입니다. 다른 앱을 종료한 후 다시 시도해주세요.';
    case 'OverconstrainedError':
      return '요청한 카메라 설정을 지원하지 않습니다. 다른 카메라 설정으로 시도합니다.';
    case 'AbortError':
      return '카메라 접근이 중단되었습니다. 다시 시도해주세요.';
    case 'NotSupportedError':
      return '이 브라우저는 카메라 기능을 지원하지 않습니다.';
    case 'SecurityError':
      return '보안상의 이유로 카메라에 접근할 수 없습니다. HTTPS 연결을 확인해주세요.';
    default:
      return error.message || '카메라 접근 중 알 수 없는 오류가 발생했습니다.';
  }
};

/**
 * 환경별 카메라 가이드 메시지 반환
 */
export const getCameraGuideMessage = (): string => {
  if (isMobileDevice()) {
    if (isIOSSafari()) {
      return 'Safari에서 카메라를 사용하려면 설정 > Safari > 카메라에서 권한을 허용해주세요.';
    }
    return 'QR 코드를 프레임 안에 맞춰주세요.';
  } else {
    return '웹에서 카메라를 사용하려면 브라우저에서 카메라 권한을 허용해주세요.';
  }
};
